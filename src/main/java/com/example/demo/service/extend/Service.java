package com.example.demo.service.extend;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thymeleaf.util.ListUtils;

import com.example.demo.entity.Entity;
import com.example.demo.form.Form;
import com.example.demo.repository.Repository;
import com.example.demo.service.AbstractService;
import com.example.demo.util.Constants;

@org.springframework.stereotype.Service
public class Service extends AbstractService<Entity> {

	/** 検索の際のinputタグを格納 */
	public static final String FORM = "form";
	/** 検索結果を格納 */
	public static final String FORM_LIST = "formList";

	@Autowired
	private Repository repository;
	/** 処理対象のデータを格納 */
	private List<Entity> entityList;

	@Override
	public void findAll() {
		List<Entity> list = repository.findAll();
		setDataList(list);
	}

	@Override
	public Entity findById(int userId) {
		Optional<Entity> dataOpt = repository.findById(Integer.valueOf(userId));
		//データがない場合はnullを返す
		return dataOpt.isPresent() ? dataOpt.get() : null;
	}

	@Override
	public void findByUserName(String userName) {
		List<Entity> list = repository.findByFullName(userName);
		setDataList(list);
	}

	@Override
	public void addUser(Form form) {
		if (isExist(form)) {
			System.err.println(getMessage(Constants.VALID_EXIST_DATA, new String[] {form.getId()}));
		} else {
			save(form);
			setAddCount(getAddCount() + 1);
		}
	}

	@Override
	public void addUser(List<Form> formList) {
		List<Form> existList = existList(formList);

		if (existList.size() == 0) {
			List<Entity> savedList = saveAll(formList);
			setAddCount(getAddCount() + savedList.size());
		} else {
			isExistDatasErrorLog(existList, true);
		}
	}

	@Override
	public void deleteUser(int userId) {
		if (isExist(userId)) {
			Integer id = Integer.valueOf(userId);
			repository.deleteById(id);
			setDeleteCount(getDeleteCount() + 1);
		} else {
			System.err.println(getMessage(Constants.VALID_NOT_EXIST_DATA, new Integer[] {userId}));
		}
	}

	@Override
	public void editUser(Form form) {
		if (isExist(form)) {
			save(form);
			setEditCount(getEditCount() + 1);
		} else {
			System.err.println(getMessage(Constants.VALID_EXIST_DATA, new String[] {form.getId()}));
		}
	}

	@Override
	public void editUser(List<Form> formList) {
		if (existList(formList).size() == 0) {
			isExistDatasErrorLog(formList, false);
		} else {
			List<Entity> savedList = saveAll(formList);
			setEditCount(getEditCount() + savedList.size());
		}
	}

	/**
	 * 共通の{@link ModelMap}をセット
	 * @param model
	 */
	public void setCommonModel(ModelMap model) {
		model.put(Constants.TITLE_KEY, Constants.SEARCH_TITLE);
	}

	/**
	 * エラーの存在をチェックする
	 * @param error
	 * @param redirect
	 * @return エラーがあればtrue
	 */
	public boolean existError(BindingResult error, RedirectAttributes redirect) {
		if (error.hasFieldErrors()) {
			StringBuilder errorMessage = new StringBuilder();
			for (var errors : error.getAllErrors()) {
				errorMessage.append(errors.getDefaultMessage());
			}
			redirect.addFlashAttribute(Constants.ERROR_MESSAGE, errorMessage.toString());
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 検索ボタン押下後のメソッド
	 * @param form 検索の際のinputタグ内の値を格納
	 * @param redirect
	 */
	public void search(Form form, RedirectAttributes redirect) {
		findByUserName(form.getFull_name());
		if (ListUtils.isEmpty(dataList)) {
			redirect.addFlashAttribute(Constants.ERROR_MESSAGE, getMessage(Constants.VALID_EMPTY_DATALIST, null));
		} else {
			redirect.addFlashAttribute(FORM_LIST, getDataList());
		}
		redirect.addFlashAttribute(FORM, form);
	}

	/**
	 * 存在する複数データのログを出力
	 * @param existList 存在する複数データ
	 * @param isExist 存在するログを出力する場合はtrue
	 */
	private void isExistDatasErrorLog(List<Form> existList, boolean isExist) {
		String[] existIdArray = new String[existList.size()];
		for (int i = 0; i < existList.size(); i++) {
			existIdArray[i] = existList.get(i).getId();
		}

		String errorMessage = String.join(", ", existIdArray);
		if (isExist) {
			System.err.println(getMessage(Constants.VALID_EXIST_DATA, new String[] {errorMessage}));
		} else {
			System.err.println(getMessage(Constants.VALID_NOT_EXIST_DATA, new String[] {errorMessage}));
		}
	}

	/**
	 * データの存在確認をする
	 * @param form 存在確認をするデータ
	 * @return 存在すればtrue
	 */
	private boolean isExist(Form form) {
		Integer ID = Integer.parseInt(form.getId());
		int id = ID.intValue();
		return findById(id) != null;
	}

	/**
	 * データの存在確認をする
	 * @param userId 存在確認をするデータのid
	 * @return 存在すればtrue
	 */
	private boolean isExist(int userId) {
		return findById(userId) != null;
	}

	/**
	 * 存在するデータのみを返す
	 * @param form 存在確認するデータの{@code List<Form}
	 * @return 存在したデータの{@code List<Form}
	 */
	private List<Form> existList(List<Form> form) {
		List<Form> existList = new ArrayList<>();

		for (Form data : form) {
			Integer ID = Integer.parseInt(data.getId());
			int id = ID.intValue();
			Entity d = findById(id);
			if (d != null) {
				existList.add(data);
			}
		}

		return existList;
	}

	/**
	 * {@link Form}クラスを{@link Entity}クラスへ変換する
	 * @param form
	 * @return 変換後の{@link Entity}クラス
	 */
	private Entity setEntity(Form form) {
		Entity entity = new Entity();

		entity.setId(Integer.parseInt(form.getId()));
		entity.setFullName(form.getFull_name());
		entity.setInsert_date(Integer.parseInt(form.getInsert_date()));

		return entity;
	}

	/**
	 * INSERTもしくはUPDATEを実行
	 * @param form 更新、挿入したいデータ
	 * @return 更新、挿入したデータの{@link Entity}クラス
	 */
	private Entity save(Form form) {
		Entity entity = setEntity(form);
		return repository.save(entity);
	}

	/**
	 * 複数のINSERTもしくはUPDATEを実施
	 * @param formList 更新、挿入したいデータ
	 * @return 更新、挿入したいデータ
	 */
	private List<Entity> saveAll(List<Form> formList) {
		setEntityListByFromList(formList);
		return repository.saveAll(entityList);
	}

	/**
	 * {@link this#entityList}を{@link Form}クラスからセットする
	 * @param formList
	 */
	private void setEntityListByFromList(List<Form> formList) {
		List<Entity> entityList = new ArrayList<>();

		for (Form form : formList) {
			entityList.add(setEntity(form));
		}

		setEntityList(entityList);
	}

	public List<Entity> getEntityList() {
		return entityList;
	}

	public void setEntityList(List<Entity> entityList) {
		this.entityList = entityList;
	}

}
