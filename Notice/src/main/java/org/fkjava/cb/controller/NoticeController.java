package org.fkjava.cb.controller;

 import java.util.List;

import org.fkjava.cb.domain.Notice;
import org.fkjava.cb.domain.NoticeRead;
import org.fkjava.cb.domain.NoticeType;
import org.fkjava.cb.service.NoticeService;
import org.fkjava.common.data.domain.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
@Controller
@RequestMapping("/notice")
public class NoticeController {
 	@Autowired
	private NoticeService noticeService;
 	
 	@GetMapping
	public ModelAndView index(//
			@RequestParam(name="pageNumber",defaultValue="0")Integer number,//
			@RequestParam(name="keyword",required=false)String keyword) {
		ModelAndView mav = new ModelAndView("notice/index");
		
		Page<NoticeRead> page = this.noticeService.findNotices(number,keyword);
		mav.addObject("page", page);
 		return mav;
	}
 	@GetMapping("add")
	public ModelAndView add() {
 		ModelAndView mav = new ModelAndView("notice/add");
 		List<NoticeType> types = noticeService.findAllTypes();
 		mav.addObject("types", types);
 		return mav;
	}
 	
 	@PostMapping()
	public ModelAndView save(Notice notice) {
 		// 保存以后，重定向到列表
 		ModelAndView mav = new ModelAndView("redirect:/notice");
		this.noticeService.write(notice);
		return mav;
	}
 	
 	/**
 	 * 阅读
 	 * 
 	 */
 	@GetMapping("{id}")
	public ModelAndView read(@PathVariable("id")String id) {
 		ModelAndView mav = new ModelAndView("notice/read");
 		Notice notice = this.noticeService.findById(id);
 		mav.addObject("notice", notice);
 		return mav;
	}
 	/**
 	 * 以阅读
 	 * @param id
 	 * @return
 	 */
 	@PostMapping("{id}")
	@ResponseBody
	public Result readed(@PathVariable("id") String id) {

		this.noticeService.read(id);

		return Result.ok();
	}

 	/**
 	 * 删除
 	 * @param id
 	 * @return
 	 */
 	@DeleteMapping("{id}")
 	@ResponseBody
	public Result delete(@PathVariable("id")String id) {
 		this.noticeService.delete(id);
		return Result.ok();
	}
 	
 	/**
 	 * 撤回
 	 * @param id
 	 * @return
 	 */
 	@GetMapping("recall/{id}")
	public String recall(@PathVariable("id")String id) {
 		this.noticeService.recall(id);
 		return "redirect:/notice";
	}
 	
 	/**
 	 * 发布
 	 * @param id
 	 * @return
 	 */
	@GetMapping("publish/{id}")
	public String publish(@PathVariable("id") String id) {
 		this.noticeService.publish(id);
 		return "redirect:/notice";
	}
	/**
	 * 编辑
	 * @param id
	 * @return
	 */
	@GetMapping("edit/{id}")
	public ModelAndView edit(@PathVariable("id") String id) {
		ModelAndView mav = new ModelAndView("notice/add");
 		Notice notice = this.noticeService.findById(id);
 		mav.addObject("notice", notice);
 		
 		List<NoticeType> types = this.noticeService.findAllTypes();
 		mav.addObject("types", types);
 		return mav;
	}
}