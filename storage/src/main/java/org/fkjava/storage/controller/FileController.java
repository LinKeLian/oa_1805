package org.fkjava.storage.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.List;

import org.fkjava.common.data.domain.Result;
import org.fkjava.storage.domain.FileInfo;
import org.fkjava.storage.service.StorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

@Controller
@RequestMapping("/storage/file")
public class FileController {
	private static final Logger LOG = LoggerFactory.getLogger(FileController.class);
	@Autowired
	private StorageService storageService;

	// 列表页面
	@GetMapping
	public ModelAndView index(//
			@RequestParam(name = "pageNumber", defaultValue = "0") Integer number, //
			@RequestParam(name = "keyword", required = false) String keyword) {

		ModelAndView mav = new ModelAndView("storage/file/index");

		Page<FileInfo> page = this.storageService.findFiles(keyword, number);
		mav.addObject("page", page);

		return mav;
	}

	@PostMapping
	public String upload(@RequestParam("file") MultipartFile file) throws IOException {

		this.wangEditorUpload(file);

		return "redirect:/storage/file";
	}
	//notice模块的图片上传
	@PostMapping("wangEditor")
	@ResponseBody
	public WangEditorResponse wangEditorUpload(@RequestParam("file") MultipartFile file) throws IOException {
		// 上传的代码、逻辑还是跟之前一样
		FileInfo info = new FileInfo();
		info.setContentType(file.getContentType());
		info.setFileSize(file.getSize());
		info.setName(file.getOriginalFilename());

		try (InputStream in = file.getInputStream()) {
			this.storageService.save(info, in);
		}

		// 只是返回加了一些内容
		WangEditorResponse wangEditorResponse = new WangEditorResponse();
		wangEditorResponse.setErrno(0);// 成功
		wangEditorResponse.getData().add("/storage/file/" + info.getId());// 图片下载的路径
		return wangEditorResponse;
	}
	//notice模块的图片上传
	public static class WangEditorResponse {
		private int errno;
		private List<String> data = new LinkedList<>();

		public int getErrno() {
			return errno;
		}

		public void setErrno(int errno) {
			this.errno = errno;
		}

		public List<String> getData() {
			return data;
		}

		public void setData(List<String> data) {
			this.data = data;
		}
	}

	@GetMapping("{id}")
	public ResponseEntity<StreamingResponseBody> download(
			// 文件的id
			@PathVariable("id") String id, //
			// 获取请求头
			@RequestHeader("User-Agent") String userAgent //
	) throws IOException {

		FileInfo fi = this.storageService.findById(id);// 读取文件信息
		if (fi == null) {
			LOG.trace("文件信息没有找到");
			return send404();
		}
		InputStream in = this.storageService.getFileContent(fi);// 读取文件内容
		if (in == null) {
			LOG.trace("文件内容没有找到");
			return send404();
		}

		// 构建响应体
		BodyBuilder builder = ResponseEntity.ok();// HTTP 200
		builder.contentLength(fi.getFileSize());// 文件大小
		// MediaType.valueOf把字符串转换为MediaType
		builder.contentType(MediaType.valueOf(fi.getContentType()));// 文件类型

		String name = fi.getName();
		// 对文件名进行编码，避免出现文件名乱码的问题
		name = URLEncoder.encode(name, "UTF-8");
		// 告诉浏览器：文件的实际名字
		// Content-Disposition用于告诉浏览器如何处理内容
		// attachment表示响应是一个附件，浏览器要保存起来，于是浏览器就会弹出文件保存窗口
		// filename告诉浏览器，文件名叫做什么！但是响应头不能有中文，而name是编码后的，没有中文的
		// *=UTF-8'' 注意：结尾是两个单引号，用于告诉浏览器文件名的编码方式
		// 【filename=文件名】这种方式可以用，但是不能兼容所有浏览器
		// 【filename*=UTF-8''】除了IE 7以下的浏览器，其他的全部兼容
		// 一般这里建议：要根据不同的浏览器判断如何返回filename
		// 获取请求头的User-Agent的值来进行判断
		builder.header("Content-Disposition", "attachment; filename*=UTF-8''" + name);
		
		/**
		 * 异步下载
		 */
		
		StreamingResponseBody body = new StreamingResponseBody() {
			@Override
			public void writeTo(OutputStream out) throws IOException {
				// 当把StreamingResponseBody作为响应体的时候，Spring MVC就会开启异步Servlet的请求
				
				// 此时相当于在这里面写代码，是在另外一个线程执行的
				
				try (InputStream input = in) {
					byte[] b = new byte[2048];
					for (int len = input.read(b); //
							len != -1; //
							len = input.read(b)//
					) {
						out.write(b, 0, len);
					}
				}
			}
		};
		ResponseEntity<StreamingResponseBody> entity = builder.body(body);

		return entity;
	}

	private ResponseEntity<StreamingResponseBody> send404() {
		BodyBuilder bodyBuilder = ResponseEntity.status(HttpStatus.NOT_FOUND);
		bodyBuilder.contentType(MediaType.valueOf("text/html;charset=UTF-8"));
		StreamingResponseBody body = (out) -> {
			out.write("文件没有找到".getBytes(Charset.forName("UTF-8")));
		};
		return bodyBuilder.body(body);
	}

	@DeleteMapping("{id}")
	@ResponseBody
	public Result delete(@PathVariable(name = "id") String id) {
		return this.storageService.deleteFile(id);
	}
}
