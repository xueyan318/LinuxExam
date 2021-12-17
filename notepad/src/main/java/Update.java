import com.google.gson.Gson;
import org.apache.commons.io.IOUtils;
import redis.clients.jedis.Jedis;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@WebServlet("/update")
public class Update extends HttpServlet {
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String URL =
            "jdbc:mysql://121.40.206.166/linux_final?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=GMT%2B8";
    static final String USER = "root";
    static final String PASS = "L@xy2016";
    static final String SQL_UPDATE_NOTEPAD  = "update t_notepad set notepadContent = ?,notepadTime = ? where id = ?;";

    static Connection conn = null;
    static Jedis jedis = null;

    // servlet创建时 初始化的东西
    public void init() {
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(URL, USER, PASS);
            jedis = new Jedis("121.40.206.166");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 当再次调用servlet销毁之前
    public void destroy() {
        try {
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 重写doPost方法
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // response 返回值类型
        response.setContentType("application/json;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        // response 返回
        PrintWriter out = response.getWriter();

        // 获取请求体中内容
        String str = IOUtils.toString(request.getInputStream(), "UTF-8");

        // 将请求体中的内容解析成实体对象
        Gson gson = new Gson();
        Notepad notepad = gson.fromJson(str, Notepad.class);

        // 调用方法
        Boolean action = update(notepad);

        // 返回结果
        out.println(action);

        out.flush();
        out.close();

    }

    // 查询 返回一个泛型为实体类的List集合
    private Boolean update(Notepad notepad) {
        Boolean flag = null;
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(SQL_UPDATE_NOTEPAD );
            // 填充占位符)
            ps.setString(1,notepad.getNotepadContent());
            ps.setString(2,notepad.getNotepadTime());
            ps.setString(3,notepad.getId());
            // 执行sql 返回影响数据库的行数
            int i = ps.executeUpdate();

            if (i > 0) { // 操作成功,数据库数据已经发生变化，需要删除缓存，为了查询能够查到新的数据
                flag = true;
                jedis.del("notepadlist");
            } else { // 操作失败
                flag = false;
            }

        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        return flag;
    }

    class Notepad {
        private String id;
        private String notepadContent;
        private String notepadTime;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getNotepadContent() {
            return notepadContent;
        }

        public void setNotepadContent(String notepadContent) {
            this.notepadContent = notepadContent;
        }

        public String getNotepadTime() {
            return notepadTime;
        }

        public void setNotepadTime(String notepadTime) {
            this.notepadTime = notepadTime;
        }
    }
}
