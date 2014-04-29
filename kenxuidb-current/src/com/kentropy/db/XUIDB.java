package com.kentropy.db;

import com.kentropy.model.KenList;
import com.kentropy.process.Process;
import java.text.DateFormat;
import java.util.Date;
import java.util.Vector;
import net.xoetrope.optional.data.sql.DatabaseTableModel;
import net.xoetrope.xml.XmlElement;
import net.xoetrope.xui.data.XModel;

public abstract interface XUIDB
{
  public abstract String getProperty(String paramString);
  
  public abstract void updateCODReport(String paramString1, String paramString2, String paramString3, String paramString4)
    throws Exception;
  
  public abstract String getICDDesc(String paramString);
  
  public abstract boolean isValidIcdAge(String paramString1, String paramString2);
  
  public abstract boolean isValidIcdSex(String paramString1, String paramString2);
  
  public abstract boolean checkEquivalence(String paramString1, String paramString2);
  
  public abstract String getImagePath();
  
  public abstract void getReportData(String paramString, XModel paramXModel, int paramInt);
  
  public abstract void getDiffDiagnosis(String paramString, XModel paramXModel);
  
  public abstract void getSearch(String paramString1, String paramString2, XModel paramXModel);
  
  public abstract void getSearchIcd(String paramString, XModel paramXModel);
  
  public abstract void getSearch(String paramString, XModel paramXModel);
  
  public abstract Vector findPhysicians(String paramString);
  
  public abstract void saveProcess(Process paramProcess);
  
  public abstract void saveProcess(Process paramProcess, String paramString);
  
  public abstract void execute(String paramString1, String paramString2, String paramString3)
    throws Exception;
  
  public abstract void saveTransition(String paramString1, String paramString2, int paramInt);
  
  public abstract String[] getNextTransition()
    throws Exception;
  
  public abstract void logAgent(String paramString1, String paramString2, String paramString3, String paramString4);
  
  public abstract Process getProcess(String paramString)
    throws Exception;
  
  public abstract XModel getTasks(String paramString1, String paramString2, String paramString3, KenList paramKenList);
  
  public abstract XModel getTasks1(String paramString1, String paramString2, String paramString3, KenList paramKenList);
  
  public abstract void getTasks(XModel paramXModel, String paramString1, String paramString2)
    throws Exception;
  
  public abstract void checkDBServer()
    throws Exception;
  
  public abstract String getTaskStatusPath(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, String paramString6);
  
  public abstract int getTaskChildren(String paramString1, String paramString2);
  
  public abstract XLogisticsModel getLogisticsM(String paramString1, String paramString2, String paramString3);
  
  public abstract XDataModel getDataM1(String paramString1, String paramString2);
  
  public abstract XDataModel getDataM2(String paramString1, String paramString2);
  
  public abstract XDataModel getDataM(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, String paramString6);
  
  public abstract void createMessage()
    throws Exception;
  
  public abstract String getLastChangeLog()
    throws Exception;
  
  public abstract String getPendingChanges()
    throws Exception;
  
  public abstract void sendServerLogs(String paramString1, String paramString2, String paramString3, String paramString4)
    throws Exception;
  
  public abstract void sendOutBoundResources()
    throws Exception;
  
  public abstract void sendOutboundLogs(String paramString)
    throws Exception;
  
  public abstract void sendOutboundLogs(String paramString1, String paramString2)
    throws Exception;
  
  public abstract void createNotification(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, String paramString6, String paramString7, String paramString8)
    throws Exception;
  
  public abstract void sendServerLogs(String paramString1, String paramString2, Vector paramVector1, Vector paramVector2, String paramString3)
    throws Exception;
  
  public abstract void sendServerLogs2(String paramString1, String paramString2, String paramString3, String paramString4)
    throws Exception;
  
  public abstract void sendServerLogs1(String paramString1, String paramString2)
    throws Exception;
  
  public abstract int sendLogs(String paramString1, String paramString2)
    throws Exception;
  
  public abstract int sendLogs(String paramString1, String paramString2, String paramString3)
    throws Exception;
  
  public abstract int sendLogs(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5)
    throws Exception;
  
  public abstract void sendLogsLocal(String paramString1, String paramString2, String paramString3)
    throws Exception;
  
  public abstract void getTask(String paramString1, String paramString2, XTaskModel paramXTaskModel, String paramString3, String paramString4, String paramString5, String paramString6, String paramString7);
  
  public abstract int getTaskChildCount(String paramString1, String paramString2, XTaskModel paramXTaskModel, String paramString3, String paramString4, String paramString5, String paramString6);
  
  public abstract void getLogisticsData(String paramString, XLogisticsModel paramXLogisticsModel);
  
  public abstract void getTaskData(String paramString, XModel paramXModel);
  
  public abstract void getTasks(String paramString1, String paramString2, XTaskModel paramXTaskModel, String paramString3, String paramString4, String paramString5, String paramString6, String paramString7);
  
  public abstract void testP(String paramString1, String paramString2, XModel paramXModel);
  
  public abstract String getPath(XModel paramXModel1, XModel paramXModel2);
  
  public abstract Vector split(String paramString1, String paramString2, int paramInt);
  
  /**
   * @deprecated
   */
  public abstract void dataPath(String paramString);
  
  public abstract DateFormat getMysqlDateFormat();
  
  public abstract int toInt(String paramString, int paramInt);
  
  public abstract Date toDatetime(String paramString, Date paramDate);
  
  public abstract String toMySQlDatetime(Date paramDate, String paramString);
  
  /**
   * @deprecated
   */
  public abstract boolean checkIfTaskCanBeSaved(String paramString1, String paramString2);
  
  public abstract int saveTaskToDb(XModel paramXModel, String paramString1, String paramString2);
  
  public abstract int saveTaskToSingle(XModel paramXModel, String paramString);
  
  public abstract DateFormat getDateFormat();
  
  public abstract XModel getAreas(String paramString1, XModel paramXModel, String paramString2)
    throws Exception;
  
  public abstract XModel getEnumData1(String paramString1, String paramString2, XModel paramXModel, String paramString3, String paramString4)
    throws Exception;
  
  public abstract XModel getAreas1(XModel paramXModel)
    throws Exception;
  
  public abstract XModel getAreadetails(String paramString);
  
  public abstract void getUsersForTeam(String paramString, KenList paramKenList);
  
  public abstract XModel getHouses(String paramString, XModel paramXModel)
    throws Exception;
  
  public abstract XModel getHouses1(String paramString, XModel paramXModel)
    throws Exception;
  
  public abstract XModel getHousedetails(String paramString1, String paramString2);
  
  public abstract XModel getHousedetails(String paramString1, String paramString2, XModel paramXModel);
  
  public abstract String getCurrentUser();
  
  public abstract void createChangeLog(String paramString1, String paramString2, Vector paramVector)
    throws Exception;
  
  public abstract void saveDataM(String paramString1, String paramString2, XModel paramXModel)
    throws Exception;
  
  public abstract void saveDataM1(String paramString1, String paramString2, XModel paramXModel)
    throws Exception;
  
  public abstract void saveDataM2(String paramString1, String paramString2, XModel paramXModel)
    throws Exception;
  
  public abstract void updateTaskStatus(XTaskModel paramXTaskModel, String paramString)
    throws Exception;
  
  public abstract void updateTaskStatus(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, String paramString6, String paramString7, String paramString8)
    throws Exception;
  
  public abstract void saveDataMUTF(String paramString1, String paramString2, XModel paramXModel)
    throws Exception;
  
  public abstract void importChangeLog(String paramString)
    throws Exception;
  
  public abstract void logImport(String paramString1, String paramString2, String paramString3)
    throws Exception;
  
  public abstract void processIncompleteImports()
    throws Exception;
  
  public abstract void processIncompleteImports1()
    throws Exception;
  
  public abstract void importChangeLogs1(String paramString)
    throws Exception;
  
  public abstract void importChangeLogs(String paramString)
    throws Exception;
  
  public abstract void distributeMessage(String paramString1, String paramString2)
    throws Exception;
  
  public abstract void deliverMessage(String paramString1, String paramString2)
    throws Exception;
  
  public abstract void updateMessageStatus(String paramString1, String paramString2)
    throws Exception;
  
  public abstract Vector getMessages(String paramString)
    throws Exception;
  
  public abstract void saveChangeLog(String paramString)
    throws Exception;
  
  public abstract void addToResourceOutboundQueue(String paramString1, String paramString2, String paramString3, String paramString4)
    throws Exception;
  
  public abstract void addToChangeLogOutboundQueue(String paramString1, String paramString2, String paramString3)
    throws Exception;
  
  public abstract void saveData(String paramString1, String paramString2, XModel paramXModel)
    throws Exception;
  
  public abstract void saveConflictData(String paramString1, String paramString2, XModel paramXModel)
    throws Exception;
  
  public abstract void saveData1(String paramString1, String paramString2, XmlElement paramXmlElement, String paramString3)
    throws Exception;
  
  public abstract void deleteResources(String paramString)
    throws Exception;
  
  public abstract void deleteAllData()
    throws Exception;
  
  public abstract void deleteResource(String paramString1, String paramString2)
    throws Exception;
  
  public abstract void deleteData(String paramString1, String paramString2, XmlElement paramXmlElement, String paramString3)
    throws Exception;
  
  public abstract void deleteData1(String paramString1, String paramString2, XmlElement paramXmlElement, String paramString3)
    throws Exception;
  
  public abstract String getTaskContext(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5);
  
  public abstract String getTaskContext(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, String paramString6);
  
  public abstract void saveTask(XTaskModel paramXTaskModel)
    throws Exception;
  
  public abstract void saveTask(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, String paramString6, XModel paramXModel)
    throws Exception;
  
  public abstract XModel getHouseholds(String paramString1, String paramString2, XModel paramXModel)
    throws Exception;
  
  public abstract XModel getHouseholddetails(String paramString1, String paramString2, String paramString3, XModel paramXModel);
  
  public abstract XModel getHouseholddetails(String paramString1, String paramString2, String paramString3);
  
  public abstract String getNextContextType(XModel paramXModel)
    throws Exception;
  
  public abstract void applyAutoUpdate(XModel paramXModel, String paramString)
    throws Exception;
  
  public abstract XModel getCMEData(XModel paramXModel, String paramString1, String paramString2)
    throws Exception;
  
  public abstract XModel getVAData(XModel paramXModel, String paramString1, String paramString2)
    throws Exception;
  
  public abstract void saveFlow(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, String paramString6, String paramString7)
    throws Exception;
  
  public abstract void saveFlowQuestion(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, String paramString6)
    throws Exception;
  
  public abstract XModel getFlowParameters(XModel paramXModel, String paramString)
    throws Exception;
  
  public abstract XModel getEnumData(XModel paramXModel, String paramString1, String paramString2)
    throws Exception;
  
  public abstract XModel getKeyValChildren(XModel paramXModel, String paramString1, String paramString2, String paramString3)
    throws Exception;
  
  public abstract XModel getISTPChildren(XModel paramXModel, String paramString1, String paramString2, String paramString3)
    throws Exception;
  
  public abstract XModel getEnumDataChildren(XModel paramXModel, String paramString1, String paramString2, String paramString3)
    throws Exception;
  
  public abstract void saveVAData(XModel paramXModel1, String paramString, XModel paramXModel2)
    throws Exception;
  
  public abstract void saveCMEData(XModel paramXModel1, String paramString, XModel paramXModel2)
    throws Exception;
  
  public abstract void saveEnumData(XModel paramXModel1, String paramString, XModel paramXModel2)
    throws Exception;
  
  public abstract String getCount(String paramString1, String paramString2);
  
  public abstract String getMaxId(XModel paramXModel, String paramString)
    throws Exception;
  
  public abstract String getMaxIndivId(String paramString1, String paramString2, String paramString3)
    throws Exception;
  
  public abstract String getMaxStdId(String paramString)
    throws Exception;
  
  public abstract String getMaxHouseId(String paramString1, String paramString2)
    throws Exception;
  
  public abstract String getMaxHouseholdId(String paramString1, String paramString2)
    throws Exception;
  
  public abstract XModel getIndividuals(String paramString1, String paramString2, String paramString3, XModel paramXModel)
    throws Exception;
  
  public abstract XModel getIndividualdetails(String paramString1, String paramString2, String paramString3, String paramString4);
  
  public abstract XModel getInterview(String paramString1, String paramString2, String paramString3, String paramString4, XModel paramXModel);
  
  public abstract XModel getCC(String paramString1, String paramString2, String paramString3, XModel paramXModel);
  
  public abstract void getDetails(DatabaseTableModel paramDatabaseTableModel, XModel paramXModel);
  
  public abstract void getProto(String paramString, XModel paramXModel);
  
  public abstract void init();
  
  public abstract void test1(String[] paramArrayOfString)
    throws Exception;
  
  public abstract String getTaskPath(String paramString);
  
  public abstract String join(Vector paramVector);
  
  public abstract void testImport(String paramString)
    throws Exception;
  
  public abstract void rollbackOutkeyvalue(String paramString1, String paramString2)
    throws Exception;
  
  public abstract void saveLogistics(XModel paramXModel)
    throws Exception;
  
  public abstract void saveTree(XModel paramXModel, String paramString1, String paramString2)
    throws Exception;
  
  public abstract void deleteKeyValue(String paramString1, String paramString2)
    throws Exception;
  
  public abstract void createKeyValueChangeLog(String paramString1, String paramString2, String paramString3)
    throws Exception;
  
  public abstract void createDeleteLog(String paramString1, String paramString2)
    throws Exception;
  
  public abstract void saveKeyValue(String paramString1, String paramString2, String paramString3)
    throws Exception;
  
  public abstract void saveKeyValue1(String paramString1, String paramString2, XModel paramXModel)
    throws Exception;
  
  public abstract void readTree(XModel paramXModel, String paramString1, String paramString2);
  
  public abstract String getKeyValuesSerialized(String paramString1, String paramString2);
  
  public abstract String getKeyValues(XModel paramXModel, String paramString1, String paramString2);
  
  public abstract String getValue(String paramString1, String paramString2);
  
  public abstract void saveInterview1(XModel paramXModel)
    throws Exception;
  
  public abstract void saveHouse(String paramString1, String paramString2, XModel paramXModel)
    throws Exception;
  
  public abstract void saveHouseHold(String paramString1, String paramString2, String paramString3, XModel paramXModel)
    throws Exception;
  
  public abstract void saveMember(String paramString1, String paramString2, String paramString3, String paramString4, XModel paramXModel)
    throws Exception;
  
  public abstract void saveVisitInfo(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, String paramString6, XModel paramXModel)
    throws Exception;
  
  public abstract void saveInterview(String paramString1, String paramString2, String paramString3, String paramString4, XModel paramXModel)
    throws Exception;
  
  public abstract void saveResponse(String paramString1, String paramString2, String paramString3, String paramString4, XModel paramXModel)
    throws Exception;
  
  public abstract void saveCommon(String paramString1, String paramString2, String paramString3, XModel paramXModel)
    throws Exception;
  
  public abstract void saveTask2(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, String paramString6, XModel paramXModel)
    throws Exception;
  
  public abstract void save(XModel paramXModel, String paramString1, String paramString2, String paramString3, String paramString4)
    throws Exception;
  
  public abstract void get(XModel paramXModel, String paramString1, String paramString2, String paramString3, String paramString4)
    throws Exception;
  
  public abstract XModel get1(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5)
    throws Exception;
  
  public abstract void authenticateUser(String paramString1, String paramString2);
  
  public abstract void authoriseUser(String paramString);
  
  public abstract boolean isPhysicianAway(String paramString1, String paramString2);
  
  public abstract boolean isPhysician(String paramString1, String paramString2);
  
  public abstract boolean updateAwayDate(String paramString, Date paramDate)
    throws Exception;
  
  public abstract void createPhysician(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, String paramString6, String paramString7)
    throws Exception;
  
  public abstract void createAccount(String paramString1, String paramString2, String paramString3)
    throws Exception;
  
  public abstract boolean physicianExists(String paramString);
  
  public abstract boolean userExists(String paramString);
  
  public abstract boolean getWorkLoad(String paramString, XModel paramXModel);
  
  public abstract boolean getPhysiciansWithLessWorkload(String paramString1, String paramString2, XModel paramXModel);
  
  public abstract int execSQL(String paramString, StringBuffer paramStringBuffer)
    throws Exception;
  
  public abstract boolean getData(String paramString1, String paramString2, String paramString3, XModel paramXModel);
  
  public abstract boolean getPhysicianDetails(String paramString, XModel paramXModel);
  
  public abstract boolean getAccountDetails(String paramString, XModel paramXModel);
  
  public abstract void getAllPhysicians(XModel paramXModel);
  
  public abstract boolean removePhysician(int paramInt);
  
  public abstract XModel getChildren(String paramString1, String paramString2, XModel paramXModel, String paramString3, String paramString4)
    throws Exception;
  
  public abstract XModel getData(String paramString1, String paramString2, XModel paramXModel, String paramString3)
    throws Exception;
  
  public abstract String getTranslation(String paramString1, String paramString2);
  
  public abstract String getTranslation1(String paramString1, String paramString2)
    throws Exception;
  
  public abstract void saveData(String paramString1, String paramString2, XModel paramXModel1, XModel paramXModel2)
    throws Exception;
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\kenxuidb-current\kenxuidb-current.jar
 * Qualified Name:     com.kentropy.db.XUIDB
 * JD-Core Version:    0.7.0.1
 */