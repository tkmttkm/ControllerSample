package com.example.demo.form;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.validation.annotation.Validated;

import io.micrometer.common.util.StringUtils;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * <pre>
 * 画面より入力された値を格納するクラス
 * その際に、NULL許容などのバリデーションもかける
 * Controllerクラすに{@link Validated}アノテーションを入れ、引数にインターフェースを入れることで、
 * どのフィールドのバリデーションをするのかを指定する
 * </pre>
 */
@Data
public class Form {

	/** すべてのフィールドのバリデーションを行う */
	public interface All{}
	/** 検索の際のバリデーションを行う */
	public interface Search{}

	/** \\d によって数値以外の入力を制限している。 */
	@NotEmpty
	@Pattern(regexp = "\\d", message = "{valid.pattern.id}", groups = All.class)
	private String id;
	@NotEmpty(message = "{valid.pattern.fullName}", groups = {Search.class, All.class})
	private String full_name;
	@Pattern(regexp = "\\d", message = "{valid.pattern.insertData}", groups = {Search.class, All.class} )
	private String insert_date;


	/**
	 * 値がセットされていない場合は、
	 * 現在日時をyyyyMMdd形式で取得
	 * @return yyyyMMdd
	 */
	public String getInsert_date() {
		if(!StringUtils.isBlank(this.insert_date)) {
			return this.insert_date;
		}

		LocalDateTime nowDate = LocalDateTime.now();
		DateTimeFormatter yyyyMMdd_formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
		String yyyyMMdd = yyyyMMdd_formatter.format(nowDate);
		return yyyyMMdd;
	}
}
