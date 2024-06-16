package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import com.example.demo.util.Constants;

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
	
	/** messages.propertiesの文言取得 */
	@Autowired
	protected MessageSource messageSource;
	
	/**
	 * messages.propertiesからメッセージ取得
	 * @param messageKey messages.propertiesのkey
	 * @param args 文言に与える引数
	 * @return メッセージ
	 */
	public String getMessage(String messageKey, Object[] args) {
		String defaultMessage = messageSource.getMessage(Constants.DEAULT_ERROR_MESSAGE, null, Locale.JAPANESE);
		return messageSource.getMessage(messageKey, args, defaultMessage, Locale.JAPANESE);
	}
	
	public int getDataListSize() {
		return dataList.size();
	}
}
