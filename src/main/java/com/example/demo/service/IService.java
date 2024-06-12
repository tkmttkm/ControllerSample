package com.example.demo.service;

import java.util.List;

import com.example.demo.entity.Entity;
import com.example.demo.form.Form;

/**
 * サービスクラスのインターフェース
 * @author Takumi
 */
public interface IService {

	/**
	 * データをすべて取得
	 */
	void findAll();
	
	/**
	 * 対象のIdのデータを取得 
	 */
	Entity findById(int userId);

	/**
	 * ユーザー名からデータを取得
	 * @param userName ユーザー名
	 */
	void findByUserName(String userName);

	/**
	 * ユーザーを1件追加（INSERT）
	 * @param form
	 */
	void addUser(Form form);

	/**
	 * ユーザーを追加
	 * @param formList 追加するユーザー
	 */
	void addUser(List<Form> formList);

	/**
	 * ユーザーを削除
	 * @param userId 削除するユーザーのID
	 */
	void deleteUser(int userId);

	/**
	 * データを１件編集（UPDATE）
	 * @param form 更新するユーザーのデータ
	 */
	void editUser(Form form);

	/**
	 * データを編集（UPDATE）
	 * @param formList 編集するデータ
	 */
	void editUser(List<Form> formList);
}
