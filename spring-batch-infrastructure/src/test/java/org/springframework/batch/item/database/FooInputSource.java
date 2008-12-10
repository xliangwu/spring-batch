package org.springframework.batch.item.database;

import javax.sql.DataSource;

import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemStream;
import org.springframework.batch.item.sample.Foo;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

@SuppressWarnings("deprecation")
class FooItemReader implements ItemStream, ItemReader<Foo>, DisposableBean, InitializingBean {

	DrivingQueryItemReader<?> itemReader;

	public void setItemReader(DrivingQueryItemReader<?> itemReader) {
		this.itemReader = itemReader;
	}

	FooDao fooDao = new SingleKeyFooDao();

	public FooItemReader(DrivingQueryItemReader<?> inputSource, DataSource dataSource) {
		this.itemReader = inputSource;
		fooDao.setDataSource(dataSource);
	}

	public Foo read() {
		Object key = itemReader.read();
		if (key != null) {
			return fooDao.getFoo(key);
		}
		else {
			return null;
		}
	}

	public void update(ExecutionContext executionContext) {
		itemReader.update(executionContext);
	}

	public void destroy() throws Exception {
		itemReader.close();
	}

	public void setFooDao(FooDao fooDao) {
		this.fooDao = fooDao;
	}

	public void afterPropertiesSet() throws Exception {
	}

	public void open(ExecutionContext executionContext) {
		itemReader.open(executionContext);
	}

	public void close() {
		itemReader.close();
	}

}
