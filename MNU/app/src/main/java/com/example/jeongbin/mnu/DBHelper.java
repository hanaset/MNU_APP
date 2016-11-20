package com.example.jeongbin.mnu;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;

/**
 * Created by JeongBin on 2016-11-20.
 */

public class DBHelper extends SQLiteOpenHelper {
    // DBHelper 생성자로 관리할 DB 이름과 버전 정보를 받음

    JSONArray busArray = null;

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    // DB를 새로 생성할 때 호출되는 함수
    @Override
    public void onCreate(SQLiteDatabase db) {
        // 새로운 테이블 생성
        /* 이름은 MONEYBOOK이고, 자동으로 값이 증가하는 _id 정수형 기본키 컬럼과
        item 문자열 컬럼, price 정수형 컬럼, create_at 문자열 컬럼으로 구성된 테이블을 생성. */
        db.execSQL("CREATE TABLE bus (go_come VARCHAR(20), location VARCHAR(20), time VARCHAR(20), start VARCHAR(20), route VARCHAR(1000));");



        try {
            PHPRequest request = new PHPRequest("http://114.70.93.130/mnu/bus.php");
            String result = request.PhPcall();

            try{
                JSONObject jsonObject = new JSONObject(result);
                busArray = jsonObject.getJSONArray("result");

                for(int i = 0; i<busArray.length() ; i++){
                    JSONObject c = busArray.getJSONObject(i);
                   // insert(db, c.getString("go_come"),c.getString("location"),c.getString("time"),c.getString("start"),c.getString("route"));
                    db.execSQL("INSERT INTO bus ('go_come', 'location', 'time', 'start', 'route') VALUES ('"+c.getString("go_come")+"','"+c.getString("location")+"','"+c.getString("time")+"','"+c.getString("start")+"','"+c.getString("route")+"')");
                }


            }catch (JSONException e){
                e.printStackTrace();
            }

        }catch (MalformedURLException e){
            e.printStackTrace();
        }
    }

    // DB 업그레이드를 위해 버전이 변경될 때 호출되는 함수
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insert(SQLiteDatabase db,String go_come, String location, String time, String start, String route) {
        // 읽고 쓰기가 가능하게 DB 열기
        db = getWritableDatabase();
        // DB에 입력한 값으로 행 추가
        db.execSQL("INSERT INTO bus ('go_come', 'location', 'time', 'start', 'route') VALUES ('"+go_come+"','"+location+"','"+time+"','"+start+"','"+route+"')");
        db.close();
    }

//    public void update(String item, int price) {
//        SQLiteDatabase db = getWritableDatabase();
//        // 입력한 항목과 일치하는 행의 가격 정보 수정
//        db.execSQL("UPDATE MONEYBOOK SET price=" + price + " WHERE item='" + item + "';");
//        db.close();
//    }
//
//    public void delete(String item) {
//        SQLiteDatabase db = getWritableDatabase();
//        // 입력한 항목과 일치하는 행 삭제
//        db.execSQL("DELETE FROM MONEYBOOK WHERE item='" + item + "';");
//        db.close();
//    }
//
//    public String getResult() {
//        // 읽기가 가능하게 DB 열기
//        SQLiteDatabase db = getReadableDatabase();
//        String result = "";
//
//        // DB에 있는 데이터를 쉽게 처리하기 위해 Cursor를 사용하여 테이블에 있는 모든 데이터 출력
//        Cursor cursor = db.rawQuery("SELECT * FROM MONEYBOOK", null);
//        while (cursor.moveToNext()) {
//            result += cursor.getString(0)
//                    + " : "
//                    + cursor.getString(1)
//                    + " | "
//                    + cursor.getInt(2)
//                    + "원 "
//                    + cursor.getString(3)
//                    + "\n";
//        }
//
//        return result;
//    }

}
