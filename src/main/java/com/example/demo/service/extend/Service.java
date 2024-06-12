package com.example.demo.service.extend;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.demo.entity.Entity;
import com.example.demo.form.Form;
import com.example.demo.repository.Repository;
import com.example.demo.service.AbstractService;

@org.springframework.stereotype.Service
public class Service extends AbstractService<Entity> {

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
			System.err.println("idが" + form.getId() + "のデータはすでに存在します。");
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
			System.err.println("idが" + userId + "のデータは存在しません。");
		}
	}

	@Override
	public void editUser(Form form) {
		if (isExist(form)) {
			save(form);
			setEditCount(getEditCount() + 1);
		} else {
			System.err.println("idが" + form.getId() + "のデータは存在しません。");
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
	 * 存在する複数データのログを出力
	 * @param existList 存在する複数データ
	 * @param isExist 存在するログを出力する場合はtrue
	 */
	private void isExistDatasErrorLog(List<Form> existList, boolean isExist) {
		String[] existId = new String[existList.size()];
		for (int i = 0; i < existList.size(); i++) {
			existId[i] = existList.get(i).getId();
		}
		if (isExist) {
			System.err.println("idが" + String.join(", ", existId) + "のデータはすでに存在します。");
		} else {
			System.err.println("idが" + String.join(", ", existId) + "のデータは存在しません。");
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
