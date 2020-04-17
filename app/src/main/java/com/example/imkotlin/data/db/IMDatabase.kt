package com.example.imkotlin.data.db

import com.example.imkotlin.extention.toVarargArray
import org.jetbrains.anko.db.MapRowParser
import org.jetbrains.anko.db.delete
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.select

//管理Contact的增删改查
class IMDatabase {

    companion object{
        val databaseHelper = DatabaseHelper()
        val instance =  IMDatabase()
    }

    //保存联系人到数据库
    fun saveContact(contact: Contact){
        databaseHelper.use {
            // 'this' is a SQLiteDatabase instance SQLiteDatabase的扩展方法
            insert(ContactTable.NAME,*contact.map.toVarargArray())
            // required: Pair<String, Any?> found: Array<Pair<String, Any?>>,所以用*对contact.map.toVarargArray()数组进行展开可变长度
            // Kotlin展开运算符*，传递一个数组参数时，需要解包数组，以便每个数组元素在函数中能作为单独的参数来调用。
            //*只支持展开的Array数组，不支持List集合
            //public fun SQLiteDatabase.insert(tableName: String,vararg values: Pair<String, Any?> ): Long
            //public fun <K, V> MutableMap<K, V>.toVarargArray(): Array<Pair<K, V>>
        }
    }

    //查询所有的联系人
    fun getAllContact(): List<Contact> = databaseHelper.use {
            select(ContactTable.NAME).parseList(object : MapRowParser<Contact>{
                override fun parseRow(columns: Map<String, Any?>): Contact = Contact(columns.toMutableMap())
            })
        //parseList函数使用了RowParser或MapRowParser函数去把cursor转换成一个对象的集合。
        //这两个不同之处就是RowParser是依赖列的顺序的，而MapRowParser是从map中拿到作为column的key名的。
    }

    //删除所有的联系人
    fun delectAllContact(){
        databaseHelper.use {
            delete(ContactTable.NAME,null,null)  //第一个参数表示表名，第二第三个参数用于约束删除某一行或几行的数据
        }
    }


}

//public class DatabaseHelper extends SQLiteOpenHelper{...}
//创建数据库，这里我们给数据库起名为“test_db”，数据库版本号为1，依靠DatabaseHelper带全部参数的构造函数创建数据库
//DatabaseHelper dbHelper = new DatabaseHelper(MainActivity.this, "test_db",null,1);
//SQLiteDatabase db = dbHelper.getWritableDatabase();

//            //插入数据按钮
//            case R.id.insert:
//                //创建存放数据的ContentValues对象
//                ContentValues values = new ContentValues();
//                values.put("name",insert_data);
//                //数据库执行插入命令
//                db.insert("user", null, values);
//                break;

//            //删除数据按钮
//            case R.id.delete:
//            db.delete("user", "name=?", new String[]{delete_data});
//            break;

//            //更新数据按钮
//            case R.id.update:
//                ContentValues values2 = new ContentValues();
//                values2.put("name", update_after_data);
//                db.update("user", values2, "name = ?", new String[]{update_before_data});
//                break;

//              //查询全部按钮
//            case R.id.query:
//                //创建游标对象
//                Cursor cursor = db.query("user", new String[]{"name"}, null, null, null, null, null);
//                //利用游标遍历所有数据对象
//                //为了显示全部，把所有对象连接起来，放到TextView中
//                String textview_data = "";
//                while(cursor.moveToNext()){
//                    String name = cursor.getString(cursor.getColumnIndex("name"));
//                    textview_data = textview_data + "\n" + name;
//                }
//                textview.setText(textview_data);
//                // 关闭游标，释放资源
//                cursor.close();
//                break;

