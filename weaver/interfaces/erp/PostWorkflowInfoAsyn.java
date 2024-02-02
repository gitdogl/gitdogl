//package weaver.interfaces.erp;
//
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//
//public class PostWorkflowInfoAsyn {
//	private ExecutorService executor = Executors.newCachedThreadPool();
//	/**
//	 *
//	 * 操作统一待办库(根据requestid删除流程)
//	 *
//	 * @param requestid
//	 */
//	public void deleteToDo(final String requestid) throws Exception {
//		executor.submit(new Runnable() {
//			public void run() {
//				try {
//					// 要执行的业务代码，我们这里没有写方法，可以让线程休息几秒进行测试
//					new Thread(new PostWorkflowInfoThread()
//							.deleteToDo(requestid)).start();
//				} catch (Exception e) {
//					throw new RuntimeException("操作统一待办库(根据requestid删除流程)报错啦！！");
//				}
//			}
//		});
//	}
//
//	/**
//	 * 操作统一待办库(根据userid删除流程)
//	 *
//	 * @param requestid
//	 */
//	public void deleteUserToDo(final String requestid, final String userid) throws Exception {
//		executor.submit(new Runnable() {
//			public void run() {
//				try {
//
//					// 要执行的业务代码，我们这里没有写方法，可以让线程休息几秒进行测试
//					new Thread(new PostWorkflowInfoThread().deleteUserToDo(requestid,
//							userid)).start();
//				} catch (Exception e) {
//					throw new RuntimeException("操作统一待办库(根据userid删除流程)报错啦！！");
//				}
//			}
//		});
//	}
//
//	/**
//	 * 强制归档，先删除，再推送
//	 *
//	 * @param requestid
//	 */
//	public void drawBackWF(final String requestid) throws Exception {
//		executor.submit(new Runnable() {
//			public void run() {
//				try {
//					// 要执行的业务代码，我们这里没有写方法，可以让线程休息几秒进行测试
//					new Thread(new PostWorkflowInfoThread().drawBackWF(requestid))
//					.start();
//				} catch (Exception e) {
//					throw new RuntimeException("操作统一待办库(根据userid删除流程)报错啦！！");
//				}
//			}
//		});
//	}
//
//	/**
//	 * 操作统一待办库(待办,已办) ERP流程状态，0：待办，1：已办，4：办结
//	 *
//	 * @param requestid
//	 */
//	public void operateToDo(final String requestid) throws Exception {
//		executor.submit(new Runnable() {
//			public void run() {
//				try {
//					// 要执行的业务代码，我们这里没有写方法，可以让线程休息几秒进行测试
//					new Thread(new PostWorkflowInfoThread().operateToDo(requestid))
//					.start();
//				} catch (Exception e) {
//					throw new RuntimeException("操作统一待办库(根据userid删除流程)报错啦！！");
//				}
//			}
//		});
//	}
//
//	/**
//	 *
//	 * 征询改待办 ERP流程状态，0：待办，1：已办，4：办结
//	 *
//	 * @param requestid
//	 * @param id
//	 */
//	public void operateZXToDo(final String requestid, final int id) throws Exception {
//		executor.submit(new Runnable() {
//			public void run() {
//				try {
//					// 要执行的业务代码，我们这里没有写方法，可以让线程休息几秒进行测试
//					new Thread(
//							new PostWorkflowInfoThread().operateZXToDo(requestid, id))
//							.start();
//				} catch (Exception e) {
//					throw new RuntimeException("操作统一待办库(根据userid删除流程)报错啦！！");
//				}
//			}
//		});
//	}
//
//	/**
//	 *
//	 * 查看变已办情景处理 ERP流程状态，0：待办，1：已办，4：办结
//	 *
//	 * @param requestid
//	 * @param userid
//	 */
//	public void ViewToHandle(final String requestid, final String userid) throws Exception {
//		executor.submit(new Runnable() {
//			public void run() {
//				try {
//					// 要执行的业务代码，我们这里没有写方法，可以让线程休息几秒进行测试
//					new Thread(new PostWorkflowInfoThread().ViewToHandle(requestid,
//							userid)).start();
//				} catch (Exception e) {
//					throw new RuntimeException("操作统一待办库(根据userid删除流程)报错啦！！");
//				}
//			}
//		});
//	}
//
//
//}
