package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.form.Form;
import com.example.demo.form.Form.Search;
import com.example.demo.service.extend.Service;

/**
 * <pre>
 * {@link org.springframework.stereotype.Controller Controller}アノテーションを付与することで、
 * DIコンテナにコントローラーとして登録される。
 * {@link RequestMapping}アノテーションを付与することで、
 * どのパスでこのコントローラーが動作するのかを指定する。
 * 今回は、パスの指定がないため、ローカル軌道で、http://localhost:8080/ でこのコントローラーが動作する
 * パスの指定をする場合は、{@code @RequestMapping("パス")}のような書き方をして指定する。
 * すると、http://localhost:8080/パス でこのコントローラーを通る
 * </pre>
 * @author Takumi
 *
 */
@org.springframework.stereotype.Controller
@RequestMapping
public class Controller {

	/** DIコンテナに登録してある{@link Service}クラスを取り出す */
	@Autowired
	private Service service;

	/**
	 * <pre>
	 * Getでリクエストが来た際にこのメソッドを通る。
	 * 今回は{@link GetMapping}にパスの指定がないため、
	 * 全体に付与されている{@link RequestMapping}のパスと合わせて、
	 * http://localhost:8080/ でgetリクエストが来た際にこのメソッドを通る。
	 * </pre>
	 * @param model viewからもらう、viewへ渡す情報のようなもの
	 * @param form modelの中でも、formというkey名がつけられているmodel
	 * @return 表示したいhtmlのファイルパス（拡張子抜きで相対パスで指定） 今回は」search.htmlを表示
	 */
	@GetMapping
	public String init(ModelMap model, @ModelAttribute(Service.FORM) Form form) {
		service.setCommonModel(model);
		return "search";
	}

	/**
	 * <pre>
	 * パス http://localhost:8080/search でpostリクエストを受けると
	 * このメソッドを通る
	 * </pre>
	 * @param form {@link Validated}の引数にインターフェースを記載することでどのフィールドをバリデーションするのかを指定する
	 * @param error formのエラーをこのerrorで受けとる
	 * @param redirect メソッドへパスを指定して投げなおす際の情報を格納する
	 * @return redirect;を指定することで、そのあとのパスの指定があるコントローラーへメソッドを投げることができる。
	 * 今回は何も指定がないため、http;//localhost:8080/でリクエストを受けた際に通るメソッドへ投げる。
	 * このControllerの場合は{@link #init(ModelMap, Form)}へ繋げる
	 */
	@PostMapping("/search")
	public String search(@Validated(Search.class) Form form, BindingResult error, RedirectAttributes redirect) {
		if (!service.existError(error, redirect)) {
			service.search(form, redirect);
		}

		return "redirect:";
	}

}
