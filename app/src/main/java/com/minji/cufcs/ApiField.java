package com.minji.cufcs;

public class ApiField {

	public static String ip = "/slfx/";
	// 登录
	public static String LOGIN = ip + "login/verifyMobile.html";
	// 登出
	public static String LOGOUT = ip + "login/logout.html";
	// 登出
	public static String INTERENTVERSIONCODE = ip + "timeData/checkUpdate.html";
	// 摄像头列表
	public static String CAMREALIST = ip + "camera/cameraList.html";
	// 防洪
	public static String FLOODCTL = ip + "floodctl/";
	// 报警
	public static String ALARM = FLOODCTL + "alarm.html";
	// 取消报警
	public static String CANCELALARM = FLOODCTL + "alarmClear.html";
	// 水情、工情所在页面
	public static String TIMEDATA = ip + "timeData/";
	// 水情
	public static String WATERCURRENTLIST = TIMEDATA + "waterDetails.html";
	// 雨情
	public static String RAINCURRENTLIST = TIMEDATA + "rainsDetails.html";
	// 机组
	public static String PUMPRUNCURRENTLIST = TIMEDATA + "unitDetails.html";
	// 阀门
	public static String GATERUNCURENTLIST = TIMEDATA + "valveDetails.html";
	// 所长执行调度
	public static String OPERATION_DISPATCHING = ip + "receiptDispatch/pageBind.html";
	// 所长执行调度详情页面
	public static String OPERATION_DISPATCHING_DETAIL = ip + "receiptDispatch/mobileRdSend.html";
	// 所长执行调度详情页面进行执行
	public static String OPERATION_DISPATCHING_DETAIL_EXECUTE = ip + "receiptDispatch/send.html";
	// 巡检页面
	public static String PATROLRECEIPT = ip + "patrolreceipt/";
	// 巡检时获取枢纽列表
	public static String POLLING_HUBNAME = ip
			+ "station/patrolnormalstation.html";
	// 巡检时获取巡检人列表
	public static String POLLING_PERSON = PATROLRECEIPT
			+ "userListByDepartment.html";
	// 巡检时进行次数验证
	public static String POLLING_NUMBER = PATROLRECEIPT + "validatenormal.html";
	// 保存巡检第一步的四个数据
	public static String POLLING_SAVE_FOUR = PATROLRECEIPT
			+ "saveMobilezb.html";
	// 保存18项中的一个
	public static String POLLING_SAVE_ONE_OF = PATROLRECEIPT
			+ "saveMobiledetials.html";
	// 查询已经保存的四项参数
	public static String POLLING_QUERY_ALREADY_SAVE = PATROLRECEIPT
			+ "patrolreceiptlist.html";
	// 请求编辑已经保存的四项参数对应的已编辑过的18项
	public static String POLLING_ALREADY_EIGHTEEN = PATROLRECEIPT
			+ "PatrolType.html";
	// 视频列表展示
	public static String SCREEN_LIST = ip + "station/stationByAppCamera.html";
	// 片区的实施列表
	public static String AREALIST = ip + "rdexecute/rdexelist.html";
	// 片区的条目的请求信息
	public static String INFORMATION = ip
			+ "rdexecute/viewrdexecutemobile.html";
	// 片区的保存三条信息
	public static String SAVEIMPLEMENTTHRID = ip
			+ "rdexecute/updateExecuteMobile.html";
	// 查询机组
	public static String INQUIREIMPLEMENTUNIT = ip + "rdexecute/findUnits.html";
	// 保存机组明细
	public static String IMPLEMENTSAVEUNIT = ip
			+ "rdexecute/saveExecuteUnitMobile.html";

	// 自主的实施列表
	public static String AUTONOMOUSLYLIST = ip + "sdexecute/executeList.html";
	// 自主的条目的请求信息
	public static String AUTONOMOUSLYINFORMATION = ip
			+ "sdexecute/viewsdexecutemobile.html";
	// 自主的保存三条信息
	public static String AUTONOMOUSLYSAVEIMPLEMENTTHRID = ip
			+ "sdexecute/updateExecuteMobile.html";
	// 自主查询机组
	public static String AUTONOMOUSLYINQUIREIMPLEMENTUNIT = ip
			+ "sdexecute/findUnit.html";
	// 自主保存机组明细
	public static String AUTONOMOUSLYIMPLEMENTSAVEUNIT = ip
			+ "sdexecute/saveExecuteUnitMobile.html";
	// 获取水情的历史曲线
	public static String REQUESTHISTORYINFORMATION = ip
			+ "historyWaterRegime/waterMobileJSON.html";
}
