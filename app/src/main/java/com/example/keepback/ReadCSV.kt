package com.example.keepback

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader

class ReadCSV(val inputstream1: InputStream, val inputstream2: InputStream, val context: Context):
    SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    //1:place, 2:quantity
    companion object{
        val DB_VERSION=1
        val DB_NAME="subwaylocker.db"
        val TABLE_NAME="locker"//->호선으로 해서 각 호선마다 테이블 만들기->생성 쿼리 따로 작성
        //col
        val ID="id" //자동부여되는 아이디
        val STATION="line" //호선
        val NAME="name" //역이름+종류
        val LOCATION="location"//위치
        val NUM="num" //총 개수
        val LARGE="large" //대
        val MEDIUM="medium" //중
        val SMALL="small" //소
        val CONTROL="control" //제어부
    }

    fun readFiles(){
        var readPlace : BufferedReader?=null
        var readQuantity:BufferedReader?=null
        try{
            readPlace=BufferedReader(InputStreamReader(inputstream1,"EUC-KR"))
            readQuantity=BufferedReader(InputStreamReader(inputstream2,"EUC-KR"))

            //read csv header
            var line1=""
            var line2=""
            do{
                line1=readPlace!!.readLine().toString()
             //   Log.i("line1",line1)
                if(line1.contains("삼전")) {
                    Log.i("저장끝",line1)
                    return
                } //9호선부터는 저장 X
                var items1=line1.split(",")

                line2=readQuantity!!.readLine().toString()
             //   Log.i("line2",line2)
                var items2=line2.split(",")

                if(items1[1].equals(items2[0])){//호선 일치 시
                    if(items1[2].equals(items2[1])){//역명 일치 시
                        //station=items1[1], name=items1[2], location=items1[3], num=items2[2].toInt(), large=items2[3].toInt(), medium=items2[4].toInt(), small=items2[5].toInt(), control=indexing
                        //LockerInfo 등록 -> sqlite에 삽입
                        try {
                            val item=LockerInfo(items1[1], items1[2], items1[3],items2[2].toInt(),items2[3].toInt(),items2[4].toInt(),items2[5].toInt(),items1[0].toInt())
                            if(!register(item)){
                                Log.e("Error : ","데이터베이스 삽입 실패")
                            }
                        }catch (e:NumberFormatException){
                            Log.e("save",items1[1]+items1[2])
                            return
                        }

                    }
                }
            }while(line1!=null && line2!=null)
        } catch (e:IOException){
            Log.e("Error : ",e.toString())
        }
    }

    override fun onCreate(db: SQLiteDatabase?) {
        //테이블 생성(호선별)
        val create_table="create table if not exists "+(TABLE_NAME)+
                " ("+ ID+" integer primary key, "+
                STATION+" text, "+NAME+" text, "+LOCATION+" text, "+
                NUM+" integer, " + LARGE+" integer, " + MEDIUM+" integer, " + SMALL+" integer, " + CONTROL+" integer)"//
      //  Log.i("table query ",create_table)
        db?.execSQL(create_table) //sql 실행 질의

    }

    private fun register(item:LockerInfo):Boolean{
        val values= ContentValues()
        values.put(STATION,item.line)
        values.put(NAME, item.name)
        values.put(LOCATION,item.location)
        values.put(NUM,item.num)
        values.put(LARGE,item.large)
        values.put(MEDIUM,item.medium)
        values.put(SMALL,item.small)
        values.put(CONTROL,item.control)
        val db=this.writableDatabase //subwaylocker.db
        if(db.insert(TABLE_NAME,null,values)>0){
            db.close()
            return true
        }

        db.close()
        return false
    }

    fun findNum(station:String):String?{ //각 역이 저장되어 있는 고유번호 찾기
        val strsql="select "+ CONTROL+" from "+TABLE_NAME+" where "+NAME+" = \'"+station+"\'"
        val db=this.readableDatabase //읽기전용 데이터베이스 객체 생성
        val cursor=db.rawQuery(strsql,null)
        if(cursor.count!=0) { //데이터 존재
            cursor.moveToFirst()
            val name= cursor?.getString(0)
            cursor.close()
            db.close()
            return name
        }
        cursor.close()
        db.close()
        return null
    }

    fun findStation(num:Int):String?{
        val strsql="select "+NAME+" from "+ TABLE_NAME+" where "+ CONTROL+" =\'"+num.toString()+"\'"
        val db=this.readableDatabase
        val cursor=db.rawQuery(strsql,null)
        if(cursor.count!=0){
            cursor.moveToFirst()
            val name=cursor?.getString(0)
            cursor.close()
            db.close()
            return name
        }
        cursor.close()
        db.close()
        return null
    }

    fun findLine(station: String):String?{
        val strsql="select "+ STATION+" from "+TABLE_NAME+" where "+NAME+" = \'"+station+"\'"
        val db=this.readableDatabase //읽기전용 데이터베이스 객체 생성
        val cursor=db.rawQuery(strsql,null)
        if(cursor.count!=0) { //데이터 존재
            cursor.moveToFirst()
            val name=cursor?.getString(0)
            cursor.close()
            db.close()
            return name
        }
        cursor.close()
        db.close()
        return null
    }

    fun findStation(station:String):ArrayList<String>?{
        var adapter= ArrayList<String>()
        val strsql="select * from "+TABLE_NAME+" where "+NAME+" = \'"+station+"\'"
        //질의문 생성(테이블의 station에 해당하는 열 가져오기)
        val db=this.readableDatabase //읽기전용 데이터베이스 객체 생성
        val cursor=db.rawQuery(strsql,null)
        if(cursor.count!=0){ //데이터 존재
            cursor.moveToFirst()
            val line=cursor.getString(1)
            val name=cursor.getString(2)
            adapter?.add("$line $name")
            val location=cursor.getString(3)
            adapter?.add("위치 : $location")
            val sum=cursor.getString(4)
            adapter?.add("총 : "+sum+"개")
            val large=cursor.getString(5)
            adapter?.add("대형 : "+large+"개")
            val medium=cursor.getString(6)
            adapter?.add("중형 : "+medium+"개")
            val small=cursor.getString(7)
            adapter?.add("소형 : "+small+"개")

            cursor.close()
            db.close()
            return adapter
        }
        cursor.close()
        db.close()
        return null
    }

    // select * from products where PNAME like '김%' //PNAME에 김으로 시작하는 모든 데이터 선택
    fun makeAdapter(linenum:String, pname: MutableList<String>): MutableList<String>? {
        //STATION(col)에서 linenum인 모든 객체 얻기

        pname.clear()
        var strsql=""
        if(linenum.equals("전체")){
            //질의문 생성(모든 테이블의 name(역이름) column을 가져오는 질의)
            strsql="select "+ NAME + " from "+TABLE_NAME
        }else{
            strsql=  "select "+NAME+" from "+ TABLE_NAME+" where "+ STATION+" = \'"+linenum+"\'"
            //질의문 생성(테이블의 STATION이 linenum에 해당하는 NAME 객체를 가져오는 질의)
        }

        val db=this.readableDatabase
        val cursor=db.rawQuery(strsql,null)
        if(cursor.count!=0){
            //showRecord(cursor)
            cursor.moveToFirst() //cursor를 data set의 처음 위치로 이동
            do{
                val name=cursor?.getString(0)//i번째 행의 이름 가져오기
                if (name != null) {

                    pname.add(name)//pname에 추가(어댑터에 만들)
                }
            }while(cursor.moveToNext())

            cursor.close()
            db.close()///????
            return pname
        }
        cursor.close()
        db.close()//////????
        return null
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }


}