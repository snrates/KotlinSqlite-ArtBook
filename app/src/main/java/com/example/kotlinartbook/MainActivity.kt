package com.example.kotlinartbook

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.ListView
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    lateinit var listView: ListView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listView=findViewById(R.id.listView)

        val artNameList=ArrayList<String>()
        val artIdList=ArrayList<Int>()

        val adapter=ArrayAdapter(this,android.R.layout.simple_list_item_1,artNameList)
        listView.adapter=adapter

        try {
            val database=this.openOrCreateDatabase("Book",Context.MODE_PRIVATE,null)

            val cursor=database.rawQuery("SELECT * FROM arts",null)
            val artNameIx=cursor.getColumnIndex("artname")
            val idIx=cursor.getColumnIndex("id")


            while (cursor.moveToNext()){
                artNameList.add(cursor.getString(artNameIx))
                artIdList.add(cursor.getInt(idIx))
            }
            adapter.notifyDataSetChanged()
            cursor.close()

        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        //Inflater -> Menu ya da xml gibi şeyleri kullanmak için kullanılır :)

        val menuInflater=menuInflater
        menuInflater.inflate(R.menu.add_art,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.add_art_item){
            val intent= Intent(this,AddActivity::class.java)
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }


}