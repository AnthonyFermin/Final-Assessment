package nyc.c4q.db;

import android.accounts.Account;
import android.app.DownloadManager;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by c4q-anthonyf on 8/30/15.
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private final static int VERSION = 1;
    private final static String MYDB = "MyDb";

    private static DatabaseHelper mHelper;

    public DatabaseHelper(Context context) {
        super(context, MYDB, null, VERSION);
    }

    public static synchronized DatabaseHelper getInstance(Context context){

        if(mHelper == null){
            mHelper = new DatabaseHelper(context.getApplicationContext());
        }

        return mHelper;

    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {

        try {
            TableUtils.createTable(connectionSource, Book.class);
            TableUtils.createTable(connectionSource, Member.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, Book.class, true);
            TableUtils.dropTable(connectionSource, Member.class, true);
            onCreate(database, connectionSource);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertRow(Book book) throws SQLException {
        getDao(Book.class).create(book);
    }

    public List<Book> loadAllBooks() throws SQLException {
        return getDao(Book.class).queryForAll();
    }

    public Member loadSpecificMember(String name) throws SQLException {

        Dao<Member, String> memberDao = DaoManager.createDao(connectionSource, Member.class);
        QueryBuilder<Member, String> queryBuilder = memberDao.queryBuilder();
        queryBuilder.where().eq(Member.NAME, name);
        PreparedQuery<Member> preparedQuery = queryBuilder.prepare();

        return memberDao.query(preparedQuery).get(0);
    }

    public void insertRow(Member member) throws SQLException {
        getDao(Member.class).create(member);
    }

    public List<Member> loadAllMembers() throws SQLException {
        return getDao(Member.class).queryForAll();
    }

}

