package com.example.dzj.mogemap.rxjava.common;

public abstract class MyHttpServices {
    protected RecordsService recordsService;
    protected WeekRecordsService weekRecordsService;
    protected MonthRecordsService monthRecordsService;
    protected CheckPhoneService checkPhoneService;
    protected AddUserService addUserService;
    protected RecordService recordService;
    protected AddRecordService addRecordService;
    protected UpdateUserService updateUserService;
    protected GetLeaderBoardsService getLeaderBoardsService;

    public abstract RecordsService getRecordsService();

    public abstract WeekRecordsService getWeekRecordsService();

    public abstract MonthRecordsService getMonthRecordsService();

    public abstract CheckPhoneService getCheckPhoneService();

    public abstract AddUserService getAddUserService();

    public abstract RecordService getRecordService();

    public abstract AddRecordService getAddRecordService();

    public abstract UpdateUserService getUpdateUserService();

    public abstract GetLeaderBoardsService getGetLeaderBoardsService();
}
