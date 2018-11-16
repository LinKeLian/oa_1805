package org.fkjava.storage.controller;

import java.io.IOException;
import java.io.InputStream;

import org.fkjava.common.data.domain.Result;
import org.fkjava.storage.domain.FileInfo;
import org.fkjava.storage.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

@Controller
@RequestMapping("/storage/file")
public class FileController {

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

		FileInfo info = new FileInfo();
		info.setContentType(file.getContentType());
		info.setFileSize(file.getSize());
		info.setName(file.getOriginalFilename());

		try (InputStream in = file.getInputStream()) {
			this.storageService.save(info, in);
		}

		return "redirect:/storage/file";
	}

	@GetMapping("{id}")
	public ResponseEntity<StreamingResponseBody> download(@PathVariable(name = "id") String id) {
		return null;
	}
	
	@DeleteMapping("{id}")
	@ResponseBody
	public Result delete(@PathVariable(name = "id") String id) {
		return null;
	}
}
