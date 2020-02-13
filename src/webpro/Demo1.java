package webpro;

// Optimistic_lock_for_db_update
// MySQL更新用的乐观锁实现

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Demo1 {

	private static ExecutorService executor = Executors.newCachedThreadPool();
	private static DbUtil dbUtil;
	private static String old_table = "oldd_goods";
	private static String new_table = "oldd_goods_new";
	
	/**
	 * 传统方式
	 * @param i
	 */
	public static void buy(int i) {
		DecimalFormat mFormat = new DecimalFormat("000"); // 确定格式，把1转换为001
		String index = mFormat.format(i);
		try {
			// 查库存是否充足
			PreparedStatement sql  = 
					dbUtil.conn.prepareStatement(
							"SELECT * FROM " + old_table + " WHERE id=1"
							);
			ResultSet res = sql.executeQuery();
			int goodsStock = 0;
			if (res.next()) {
				goodsStock = res.getInt("goodsStock");
			}
			if (goodsStock > 0) {
				// 有库存
				goodsStock--;
				// 更新库存
				PreparedStatement sql1  = 
						dbUtil.conn.prepareStatement(
								"UPDATE " + old_table + " SET `goodsStock`=? WHERE  id=1"
								);
				sql1.setInt(1, goodsStock);
				sql1.executeUpdate();
				System.out.println("user[" + index + "] buy goods success.");
			} else {
				// 卖没了
				System.out.println("user[" + index + "] buy goods fail, out of stock.");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	/**
	 * CAS机制
	 * @param i
	 */
	public static void newBuy(int i) {
		DecimalFormat mFormat = new DecimalFormat("000"); // 确定格式，把1转换为001
		String index = mFormat.format(i);
		try {
			// 查库存是否充足
			PreparedStatement sql  = 
					dbUtil.conn.prepareStatement(
							"SELECT * FROM " + new_table + " WHERE id=1"
							);
			ResultSet res = sql.executeQuery();
			int goodsStock = 0;
			int version = 0;
			if (res.next()) {
				goodsStock = res.getInt("goodsStock");
				// 获取当前数据的版本号
				version    = res.getInt("version");
			}
			// 计算存入数据库后的版本号
			int newVersion = version + 1;
			if (goodsStock > 0) {
				// 有库存
				goodsStock--;
				// 更新库存
				PreparedStatement sql1  = 
						dbUtil.conn.prepareStatement(
								"UPDATE " + new_table + " SET goodsStock=?, version=? WHERE id=1 AND version=?"
								);
				sql1.setInt(1, goodsStock);
				sql1.setInt(2, newVersion);
				sql1.setInt(3, version);
				int affectRows = sql1.executeUpdate();
				if (affectRows > 0) {
					// 成功
					System.out.println("user[" + index + "] buy goods success.");
				} else {
					// 异常处理
					System.out.println("user[" + index + "] flash sale fail, please try again later.");
				}
			} else {
				// 卖没了
				System.out.println("user[" + index + "] buy goods fail, out of stock.");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void resetDbData() {
		try {
			PreparedStatement sql  = 
					dbUtil.conn.prepareStatement(
							"UPDATE " + new_table + " SET goodsStock=5, version=0 WHERE id=1"
							);
			PreparedStatement sql1  = 
					dbUtil.conn.prepareStatement(
							"UPDATE " + old_table + " SET goodsStock=5 WHERE id=1"
							);
			sql.executeUpdate();
			sql1.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		// ---------模拟person个人并发抢够--------
		int preson = 20;
		// ---------模拟person个人并发抢够--------
		
		
		dbUtil = new DbUtil();
		resetDbData();

		for (int i = 0; i < preson; i++) {
			int currentIndex = i;
			executor.submit(new Runnable() {

				@Override
				public void run() {
//					buy(currentIndex); // 传统开发方式
					newBuy(currentIndex); // 加上版本号进行并发控制
				}
				
			});

		}

		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		executor.shutdown();
		System.out.println("executor: shutdown");

	}

}
