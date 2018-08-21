package com.example.dzj.mogemap.rxjava.common;

import com.example.dzj.mogemap.modle.MogeLeaderboards;
import com.example.dzj.mogemap.modle.Mogemap_run_record;
import com.example.dzj.mogemap.utils.HttpUtil;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface GetLeaderBoardsService {
    @GET(HttpUtil.GET_LEADERBOARDS)
    Observable<MogeLeaderboards> getLeaderBoards(@Path("mPhone") String mPhone, @Path("mType") int mType);
}
