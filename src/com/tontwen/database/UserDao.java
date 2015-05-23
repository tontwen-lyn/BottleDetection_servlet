package com.tontwen.database;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import com.sun.crypto.provider.RSACipher;
import com.tontwen.bottledetection.BottleDetectNumber_RptNo;
import com.tontwen.bottledetection.BottleInfo;
import com.tontwen.bottledetection.GlobalDetectWaitedBottle;
import com.tontwen.bottledetection.ChubuPanduanResult;
import com.tontwen.bottledetection.OperatorInfo;
import com.tontwen.bottledetection.BottleInfo_CarInfo;
import com.tontwen.database.DBUtil;

@SuppressWarnings("unused")
public class UserDao {
	//login
	public boolean isLoginSuccess(OperatorInfo op){
		boolean result = true;
		String sql = "select * from OperatorInfo where Operatornumber=? and OperatorPwd=?";
		String[] parameters = {op.getOperatorNumber(),op.getOperatorPwd()};
		//System.out.println(op.getOperatorNumber()+" "+op.getOperatorPwd());
		ResultSet rs = DBUtil.executeQuery(sql, parameters);
		try {
			if(rs.next()){
				result = true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result = false;
		}finally{
			DBUtil.close(DBUtil.getConn(), DBUtil.getPs(), DBUtil.getRs());
			try {
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return result;
	}

	//add new bottle
	public boolean executeAddBottleInfoCarInfo(BottleInfo bi){
		boolean result = true;
		String sql = "insert into BottleInfo_CarInfo(BottleNumber,CarNumber,BottleType,BottleMadeCountry,BottleMadeCompany,BottleMadeCompanyID,BottleMadeLicense,BottleNominalPressure,BottleWaterTestPressure,BottleDesignThickness,BottleActualWeight,BottleActualVolume,BottleMadeDate,BottleFirstInstallDate,BottleLastCheckDate,BottleNextCheckDate,BottleServiceYears,BottleBelonged,SaveDate,HasDeleted,BottleLicense,BottleGuige,BottleInstall,BottleStdVol) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,0,?,?,?,?)";
		String[] parameters = {bi.getBottleNumber(),bi.getCarNumber(),Integer.toString(bi.getBottleType()),bi.getBottleMadeCountry(),bi.getBottleMadeCompany(),bi.getBottleMadeCompanyID(),bi.getBottleMadeLicense(),bi.getBottleNominalPressure(),bi.getBottleWaterTestPressure(),bi.getBottleDesignThickness(),bi.getBottleActualWeight(),bi.getBottleActualVolume(),bi.getBottleMadeDate(),bi.getBottleFirstInstallDate(),bi.getBottleLastCheckDate(),bi.getBottleNextCheckDate(),Integer.toString(bi.getBottleServiceYears()),bi.getBottleBelonged(),bi.getSaveDate(),bi.getBottleLicense(),bi.getBottleGuide(),bi.getBottleInstall(),bi.getBottleStdVol()};
		try{
			DBUtil.executeUpdate(sql, parameters);
		}catch(Exception e){
			e.printStackTrace();
			result = false;
		}finally{
			DBUtil.close(DBUtil.getConn(), DBUtil.getPs(), DBUtil.getRs());
		}
		return result;
	}

	//all bottles by page
	public ArrayList<BottleInfo_CarInfo> executeAllBottleQueryByPage(int pageNow,int pageSize){
		ArrayList<BottleInfo_CarInfo> list  = new ArrayList<BottleInfo_CarInfo>();
		String sql = "select * from (select row_number()over(order by SaveDate DESC)rownumber,* from BottleDetectionLine.dbo.BottleInfo_CarInfo)a where rownumber between ? and ?";
		String[] parameters= {((pageNow-1)*pageSize+1)+"",(pageNow*pageSize)+""};
		ResultSet rs = DBUtil.executeQuery(sql, parameters);
		try {
			while(rs.next()){
				BottleInfo_CarInfo bc = new BottleInfo_CarInfo();
				bc.setBottleNumber(rs.getString("BottleNumber"));
				bc.setBottleType(rs.getInt("BottleType"));
				bc.setBottleMadeCountry(rs.getString("BottleMadeCountry"));
				bc.setBottleMadeCompany(rs.getString("BottleMadeCompany"));
				bc.setBottleMadeCompanyID(rs.getString("BottleMadeCompanyID"));
				bc.setBottleMadeLicense(rs.getString("BottleMadeLicense"));
				bc.setBottleBelonged(rs.getString("BottleBelonged"));
				bc.setBottleServiceYears(rs.getInt("BottleServiceYears"));
				bc.setBottleNominalPressure(rs.getString("BottleNominalPressure"));
				bc.setBottleWaterTestPressure(rs.getString("BottleWaterTestPressure"));
				bc.setBottleDesignThickness(rs.getString("BottleDesignThickness"));
				bc.setBottleActualWeight(rs.getString("BottleActualWeight"));
				bc.setBottleActualVolume(rs.getString("BottleActualVolume"));
				//bc.setBottleNominalVolume(rs.getString("BottleNominalVolume"));
				bc.setBottleMadeDate(rs.getString("BottleMadeDate"));
				bc.setBottleFirstInstallDate(rs.getString("BottleFirstInstallDate"));
				bc.setBottleLastCheckDate(rs.getString("BottleLastCheckDate"));
				bc.setBottleNextCheckDate(rs.getString("BottleNextCheckDate"));
				bc.setBottleLicense(rs.getString("BottleLicense"));
				//bc.setBottleGuide(rs.getString("BottleGuide"));
				bc.setBottleInstall(rs.getString("BottleInstall"));
				bc.setCarNumber(rs.getString("CarNumber"));
				bc.setCarType(rs.getInt("CarType"));
				bc.setCarMadeFactory(rs.getString("CarMadeFactory"));
				bc.setCarBelongedName(rs.getString("CarBelongedName"));
				bc.setCarBelongedTel(rs.getString("CarBelongedTel"));
				bc.setCarBelongedCompany(rs.getString("CarBelongedCompany"));
				bc.setCarBelongedCompanyAddress(rs.getString("CarBelongedCompanyAddress"));
				list.add(bc);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			DBUtil.close(DBUtil.getConn(), DBUtil.getPs(), DBUtil.getRs());
			try {
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return list;
	}

	//count pages
	public int getPageCount(int pageSize){
		int rowCount = 0;
		String sql = "select count(*) from users"; //get the number of all records
		ResultSet rs = null;;

		try {
			rs = DBUtil.executeQuery(sql, null);
			rs.next();
			rowCount = rs.getInt(1); //getInt is a method used to get the value which is an Int of the column designated in ()
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			DBUtil.close(DBUtil.getConn(), DBUtil.getPs(), DBUtil.getRs());
			try {
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return (rowCount-1)/pageSize+1;
	}

	//count records
	public int getCount(String condition){
		int Count=0;
		String sql="select count(name) where "+condition+"=?";
		ResultSet rs = null;	
		try {
			rs = DBUtil.executeQuery(sql, null);
			rs.next();
			Count = rs.getInt(1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			DBUtil.close(DBUtil.getConn(), DBUtil.getPs(), DBUtil.getRs());
			try {
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return Count;
	}
	
	//return bottles waiting in first-step detection
	public ArrayList<BottleInfo_CarInfo> executeQueryBottleCP(){
		ArrayList<BottleInfo_CarInfo> list = new ArrayList<BottleInfo_CarInfo>();
		String sql = "select * from BottleDetectionLine.dbo.BottleInfo_CarInfo where BottleNumber not in (select bottlenumber from BottleDetectionLine.dbo.BottleInfo_BottleDectectInfo)";
		ResultSet rs = DBUtil.executeQuery(sql,null);
		try {
			while(rs.next()){
				BottleInfo_CarInfo bc = new BottleInfo_CarInfo();
				bc.setBottleNumber(rs.getString("BottleNumber"));
				bc.setCarNumber(rs.getString("CarNumber"));
				bc.setRfidNumber(rs.getString("RFIDNumber"));
				bc.setBottleMadeCountry(rs.getString("BottleMadeCountry"));
				bc.setBottleType(rs.getInt("BottleType"));
				bc.setBottleTypeC(rs.getString("BottleTypeC"));
				bc.setBottleMadeCompany(rs.getString("BottleMadeCompany"));
				bc.setBottleMadeCompanyID(rs.getString("BottleMadeCompanyID"));
				bc.setBottleMadeLicense(rs.getString("BottleMadeLicense"));
				bc.setBottleNominalPressure(rs.getString("BottleNominalPressure"));
				bc.setBottleWaterTestPressure(rs.getString("BottleWaterTestPressure"));
				bc.setBottleDesignThickness(rs.getString("BottleDesignThickness"));
				bc.setBottleActualWeight(rs.getString("BottleActualWeight"));
				bc.setBottleActualVolume(rs.getString("BottleActualVolume"));
				bc.setBottleMadeDate(rs.getString("BottleMadeDate"));
				bc.setBottleFirstInstallDate(rs.getString("BottleFirstInstallDate"));
				bc.setBottleLastCheckDate(rs.getString("BottleLastCheckDate"));
				bc.setBottleNextCheckDate(rs.getString("BottleNextCheckDate"));
				bc.setBottleServiceYears(rs.getInt("BottleServiceYears"));
				bc.setBottleBelonged(rs.getString("BottleBelonged"));
				bc.setSaveDate(rs.getString("SaveDate"));
				bc.setHasDeleted(rs.getInt("HasDeleted"));
				bc.setBottleLicense(rs.getString("BottleLicense"));
				bc.setBottleGuige(rs.getString("BottleGuige"));
				bc.setBottleInstall(rs.getString("BottleInstall"));
				bc.setBottleStdVol(rs.getString("BottleStdVol"));
				bc.setCarInfoID(rs.getString("CarInfoID"));
				bc.setCarType(rs.getInt("CarType"));
				bc.setCarTypeC(rs.getString("CarTypeC"));
				bc.setCarBelongedName(rs.getString("CarBelongedName"));
				bc.setCarMadeFactory(rs.getString("CarMadeFactory"));
				bc.setCarBelongedTel(rs.getString("CarBelongedTel"));
				bc.setCarBelongedCompanyAddress(rs.getString("CarBelongedCompanyAddress"));
				bc.setCarBelongedCompany(rs.getString("CarBelongedCompany"));
				list.add(bc);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			DBUtil.close(DBUtil.getConn(), DBUtil.getPs(), DBUtil.getRs());
			try {
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return list;
	}
	
	//Execute first-step detection, and return bottle detection number, as well as report number
	public BottleDetectNumber_RptNo executeChubuPanduan(ChubuPanduanResult cpr){
		
		BottleDetectNumber_RptNo bnrn = new BottleDetectNumber_RptNo();
		UserDao ud = new UserDao();
		String bdn = ud.generateBottleDetectNumber(cpr.getBottleType());
		String rptNo = ud.generateRptNo(cpr.getCarNumber());
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd"); 
		String nowTime = simpleDateFormat.format(new java.util.Date());
		
		String sql = "";
		String[] parameters = {bdn,cpr.getBottleNumber(),nowTime,rptNo};
		
		if (cpr.getPreDetectResult() == "0") {
			sql = "insert BottleDetectionLine.dbo.BottleDetectInfo(BottleDetectNumber,BottleNumber,FinalDetectResult,HasInstalled,BottleDetectStatus,PreDetectResult,PreDetectDetail,PreDetectOver,PreDetectOperator,PreDetectDate,GlobalDetectOver,NoneDestructiveOver,WeightVacuumOver,WaterTestOver,ThicknessResult,InnerDryOver,BottleValveChangeOver,AirProofTestResult,AirProofTestOver,VacuumPressure,VacuumOver,PreMemo,GlobalSub1,GlobalSub2,GlobalSub3,GlobalSub4,GlobalSub5,GlobalSub6,HasWriteRFID,RptNo,OperateState,HasPrint,CheckState) values(?,?,'-',0,0,1,'',1,'管理员',?,0,0,0,0,2,0,0,'待检',0,0.09,0,'',1,1,1,1,1,1,0,?,0,0,0)";
			
		}else if (cpr.getPreDetectResult() == "1") {
			sql = "insert BottleDetectionLine.dbo.BottleDetectInfo(BottleDetectNumber,BottleNumber,FinalDetectResult,FinalDetectDate,HasInstalled,BottleDetectStatus,PreDetectResult,PreDetectDetail,PreDetectOver,PreDetectOperator,PreDetectDate,GlobalDetectOver,NoneDestructiveOver,WeightVacuumOver,WaterTestOver,ThicknessResult,InnerDryOver,BottleValveChangeOver,AirProofTestResult,AirProofTestOver,VacuumOver,PreMemo,GlobalSub1,GlobalSub2,GlobalSub3,GlobalSub4,GlobalSub5,GlobalSub6,HasWriteRFID,FailPos,RptNo,OperateState,HasPrint,CheckState) values(?,?,'判废',?,0,0,0,'气瓶标志不清晰',1,'管理员',?,0,0,0,0,2,0,0,'待检',0,0,'',1,1,1,1,1,1,0,'CP',?,0,0,0)";
		}
		try{
			DBUtil.executeUpdate(sql, parameters);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			DBUtil.close(DBUtil.getConn(), DBUtil.getPs(), DBUtil.getRs());
		}
		bnrn.setBottleDetectNumber(bdn);
		bnrn.setRptNo(rptNo);
		return bnrn;
	}

	//generate Bottle Detection Number
	private String generateBottleDetectNumber(String bottleType) {
		String tmpNumber;
		String maxNumber = "";
		int maxNumberInt;
		
		//generate xx15000001
		if (bottleType == "0") {
			tmpNumber = "CR";
		}else if (bottleType == "1") {
			tmpNumber = "GP";
		}else {
			tmpNumber = "WZ";
		}
		
		//generate GPxx000001
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd"); 
		String nowTime = simpleDateFormat.format(new java.util.Date());
		tmpNumber = tmpNumber + nowTime.substring(2, 3);
		
		String sql = "select max(BottleDetectNumber)as max_CLN from BottleDetectionLine.dbo.BottleDetectInfo where BottleDetectNumber like '?%'";
		String[] parameters = {tmpNumber};
		ResultSet rs = DBUtil.executeQuery(sql, parameters);
		try {
			if(rs.next()){
				maxNumber = rs.getString("max_CLN");
			}else {
				maxNumber = tmpNumber.substring(0,1) + nowTime.substring(2, 3) + "000000";
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//result = false;
		}finally{
			DBUtil.close(DBUtil.getConn(), DBUtil.getPs(), DBUtil.getRs());
			try {
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//generate GP15xxxxxx
		maxNumberInt = Integer.parseInt(maxNumber.substring(4)) + 1;
		if (maxNumberInt < 10) {
			tmpNumber = tmpNumber + "00000" + Integer.toString(maxNumberInt);
		}else if (maxNumberInt < 100) {
			tmpNumber = tmpNumber + "0000" + Integer.toString(maxNumberInt);
		}else if (maxNumberInt < 1000) {
			tmpNumber = tmpNumber + "000" + Integer.toString(maxNumberInt);
		}else if (maxNumberInt < 10000) {
			tmpNumber = tmpNumber + "00" + Integer.toString(maxNumberInt);
		}else if (maxNumberInt < 100000) {
			tmpNumber = tmpNumber + "0" + Integer.toString(maxNumberInt);
		}else if (maxNumberInt < 1000000) {
			tmpNumber = tmpNumber + Integer.toString(maxNumberInt);
		}
		
		return tmpNumber;
	}
	
	//generate Report Number
	private String generateRptNo(String carNumber) {
		String maxNumber;
		int maxNumberInt;
		String tmpNumber;
		
		//tmpNumber generates in the format QP2015
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd"); 
		String nowTime = simpleDateFormat.format(new java.util.Date());
		tmpNumber = "QP" + nowTime.substring(0, 3);
		
		String sql = "select max(RptNo)as max_CLN from BottleDetectionLine.dbo.RptInfo where RptNo like 'QP?%' and CarNumber = ? and FinalDetectResult = '-'";
		String[] parameters = {nowTime.substring(0, 3),carNumber};
		ResultSet rs = DBUtil.executeQuery(sql, parameters);
		try {
			if(rs.next()){
				//get max report number of this car
				tmpNumber = rs.getString("max_CLN");
			}else {
				//get max report number in BottleDetectInfo
				sql = "select max(RptNo)as max_CLN from BottleDetectionLine.dbo.BottleDetectInfo where RptNo like '?%'";
				String[] para2 = {nowTime.substring(0, 3)};
				rs = DBUtil.executeQuery(sql, parameters);
				maxNumber = rs.getString("max_CLN");
				
				//generate GP2015xxxxxx
				maxNumberInt = Integer.parseInt(maxNumber.substring(6)) + 1;
				if (maxNumberInt < 10) {
					tmpNumber = tmpNumber + "00000" + Integer.toString(maxNumberInt);
				}else if (maxNumberInt < 100) {
					tmpNumber = tmpNumber + "0000" + Integer.toString(maxNumberInt);
				}else if (maxNumberInt < 1000) {
					tmpNumber = tmpNumber + "000" + Integer.toString(maxNumberInt);
				}else if (maxNumberInt < 10000) {
					tmpNumber = tmpNumber + "00" + Integer.toString(maxNumberInt);
				}else if (maxNumberInt < 100000) {
					tmpNumber = tmpNumber + "0" + Integer.toString(maxNumberInt);
				}else if (maxNumberInt < 1000000) {
					tmpNumber = tmpNumber + Integer.toString(maxNumberInt);
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//result = false;
		}finally{
			DBUtil.close(DBUtil.getConn(), DBUtil.getPs(), DBUtil.getRs());
			try {
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return tmpNumber;
	}
	
	//get bottles waiting in global detection
	public ArrayList<GlobalDetectWaitedBottle> executeAllGlobalDetectWaitedBottleQuery(){
		ArrayList<GlobalDetectWaitedBottle> list  = new ArrayList<GlobalDetectWaitedBottle>();
		String sql ="select BottleDetectNumber ,BottleNumber ,CarNumber ,BottleType  from dbo.BottleInfo_BottleDectectInfo "
				+ "where PreDetectOver =1 and GlobalDetectOver=0";
		ResultSet rs = DBUtil.executeQuery(sql, null);
		try {
			while(rs.next()){
				GlobalDetectWaitedBottle gd = new GlobalDetectWaitedBottle();
				gd.setBottleNumber(rs.getString("BottleNumber"));
				gd.setBottleType(rs.getInt("BottleType"));
				gd.setBottleDetectNumber(rs.getString("BottleDetectNumber"));
				gd.setCarNumber(rs.getString("CarNumber"));
				list.add(gd);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			DBUtil.close(DBUtil.getConn(), DBUtil.getPs(), DBUtil.getRs());
			try {
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return list;
	}

	/*public Staff execute(Staff user){
		Staff u = new Staff();
		String sql = "select * from users where username=? and password=?";
		String[] parameters = {u.getName()};
		ResultSet rs = DBUtil.executeQuery(sql, parameters);
		try {
			if(rs.next()){
				u.setId(rs.getInt("id"));
				u.setName(rs.getString("username"));
				u.setPassword(rs.getString("password"));
				u.setEmail(rs.getString("email"));
				u.setHiredate(rs.getString("hiredate"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			DBUtil.close(DBUtil.getConn(), DBUtil.getPs(), DBUtil.getRs());
			try {
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return u;
	}

	public ArrayList<Staff> executeQueryOnCondition(String attribute,String text){
		ArrayList<Staff> list  = new ArrayList<Staff>();
		String sql = "select * from Staff where "+attribute+"=?";
		String[] parameters = {text};
		ResultSet rs = DBUtil.executeQuery(sql, parameters);
		try {
			while(rs.next()){
				Staff user = new Staff();
				user.setName(rs.getString("name"));
				user.setSex(rs.getString("sex"));
				user.setHometown(rs.getString("hometown"));
				user.setStaffno(rs.getString("staffno"));
				user.setTelephonenumber(rs.getString("telephonenumber"));
				user.setId(rs.getString("id"));
				user.setEducation(rs.getString("education"));
				user.setSchool(rs.getString("school"));
				user.setSchooltype(rs.getString("schooltype"));
				user.setMajor(rs.getString("major"));
				user.setForeignlanguagelevel(rs.getString("foreignlanguagelevel"));
				user.setGraduationdate(rs.getString("graduationdate"));
				user.setTd(rs.getString("td"));
				user.setEpw(rs.getString("epw"));
				user.setExp(rs.getString("exp"));
				user.setTeamname(rs.getString("teamname"));
				user.setSenddate(rs.getString("senddate"));
				user.setStafftype(rs.getString("stafftype"));
				user.setProject(rs.getString("project"));
				user.setExd(rs.getString("exd"));
				user.setQexd(rs.getString("qexd"));
				user.setStaffcondition(rs.getString("staffcondition"));
				list.add(user);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			DBUtil.close(DBUtil.getConn(), DBUtil.getPs(), DBUtil.getRs());
			try {
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return list;
	}

	public ArrayList<Staff> executeQuery(){
		ArrayList<Staff> list  = new ArrayList<Staff>();
		String sql = "select * from Staff";

		ResultSet rs = DBUtil.executeQuery(sql, null);
		try {
			while(rs.next()){
				Staff user = new Staff();
				user.setName(rs.getString("name"));
				user.setSex(rs.getString("sex"));
				user.setHometown(rs.getString("hometown"));
				user.setStaffno(rs.getString("staffno"));
				user.setTelephonenumber(rs.getString("telephonenumber"));
				user.setId(rs.getString("id"));
				user.setEducation(rs.getString("education"));
				user.setSchool(rs.getString("school"));
				user.setSchooltype(rs.getString("schooltype"));
				user.setMajor(rs.getString("major"));
				user.setForeignlanguagelevel(rs.getString("foreignlanguagelevel"));
				user.setGraduationdate(rs.getString("graduationdate"));
				user.setTd(rs.getString("td"));
				user.setEpw(rs.getString("epw"));
				user.setExp(rs.getString("exp"));
				user.setTeamname(rs.getString("teamname"));
				user.setSenddate(rs.getString("senddate"));
				user.setStafftype(rs.getString("stafftype"));
				user.setProject(rs.getString("project"));
				user.setExd(rs.getString("exd"));
				user.setQexd(rs.getString("qexd"));
				user.setStaffcondition(rs.getString("staffcondition"));
				list.add(user);

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			DBUtil.close(DBUtil.getConn(), DBUtil.getPs(), DBUtil.getRs());
			try {
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return list;
	}

	public boolean executeDelete(String userid){
		boolean result = true;
		String sql = "delete from STAFF where STAFFNO=?";
		String[] parameters={userid};
		try{
			DBUtil.executeUpdate(sql,parameters);
		}catch(Exception e){
			e.printStackTrace();
			result = false;
		}finally{
			DBUtil.close(DBUtil.getConn(), DBUtil.getPs(), DBUtil.getRs());
		}
		return result;
	}

	public boolean executeUpdate(Staff user){
		boolean result = true;
		System.out.println(user.getStaffno());
		String sql = "update STAFF set NAME=?,sex=?,hometown=?,telephonenumber=?,id=?,education=?,school=?,schooltype=?,major=?,foreignlanguagelevel=?,graduationdate=?,td=?,epw=?,exp=?,teamname=?,senddate=?,stafftype=?,project=?,exd=?,qexd=?,staffcondition=? where staffno=?";
		String[] parameters = {user.getName(),user.getSex(),user.getHometown(),user.getTelephonenumber(),
				user.getId(),user.getEducation(),user.getSchool(),user.getSchooltype(),user.getMajor(),
				user.getForeignlanguagelevel(),user.getGraduationdate(),user.getTd(),user.getEpw(),user.getExp(),
				user.getTeamname(),user.getSenddate(),user.getStafftype(),user.getProject(),user.getExd(),user.getQexd(),user.getStaffcondition(),user.getStaffno()};

		try{
			DBUtil.executeUpdate(sql,parameters);
		}catch(Exception e){
			e.printStackTrace();
			result = false;
		}finally{
			DBUtil.close(DBUtil.getConn(), DBUtil.getPs(), DBUtil.getRs());
		}
		System.out.println(result);
		return result;
	}

	public Staff getUserById(String staffno){
		Staff user = new Staff();
		String sql = "select * from Staff where staffno=?";
		String[] parameters={staffno};
		ResultSet rs = DBUtil.executeQuery(sql, parameters);
		try {
			if(rs.next()){
				user.setName(rs.getString("name"));
				user.setSex(rs.getString("sex"));
				user.setHometown(rs.getString("hometown"));
				user.setTelephonenumber(rs.getString("telephonenumber"));
				user.setId(rs.getString("id"));
				user.setEducation(rs.getString("education"));
				user.setSchool(rs.getString("school"));
				user.setSchooltype(rs.getString("schooltype"));
				user.setMajor(rs.getString("major"));
				user.setForeignlanguagelevel(rs.getString("foreignlanguagelevel"));
				user.setGraduationdate(rs.getString("graduationdate"));
				user.setTd(rs.getString("td"));
				user.setEpw(rs.getString("epw"));
				user.setExp(rs.getString("exp"));
				user.setTeamname(rs.getString("teamname"));
				user.setSenddate(rs.getString("senddate"));
				user.setStafftype(rs.getString("stafftype"));
				user.setProject(rs.getString("project"));
				user.setExd(rs.getString("exd"));
				user.setQexd(rs.getString("qexd"));
				user.setStaffcondition(rs.getString("staffcondition"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			DBUtil.close(DBUtil.getConn(), DBUtil.getPs(), DBUtil.getRs());
			try {
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return user;
	}*/
}