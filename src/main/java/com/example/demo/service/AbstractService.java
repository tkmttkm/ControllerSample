package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

/**
 * <pre>
 * サービスクラスの抽象クラス
 * このクラスを継承してサービスクラスを作成
 * </pre>
 * @author Takumi
 *
 * @param <T> 作成するサービスクラスで使用するEntityクラスを渡す
 */
@Data
public abstract class AbstractService<T> implements IService {

	/** 取得したデータ */
	protected List<T> dataList = new ArrayList<T>();
	
	/** 取得したデータ数 */
	protected int dataListSize;
	
	/** 追加したデータ数 */
	protected int addCount;
	
	/** 削除したデータ数 */
	protected int deleteCount;
	
	/** 編集したデータ数 */
	protected int editCount;
	
	public int getDataListSize() {
		return dataList.size();
	}
}
