import com.john.dao.IUserDao;
import com.john.io.Resources;
import com.john.builder.User;
import com.john.sqlSession.SqlSession;
import com.john.sqlSession.SqlSessionFactory;
import com.john.sqlSession.SqlSessionFactoryBuilder;
import org.dom4j.DocumentException;
import org.junit.Test;

import java.beans.IntrospectionException;
import java.beans.PropertyVetoException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.List;

/**
 * @author:wenwei
 * @date:2020/02/21
 * @description:
 */
public class IPersistenceTest {
    @Test
    public void test() throws PropertyVetoException, DocumentException, IllegalAccessException, IntrospectionException, InstantiationException, NoSuchFieldException, SQLException, InvocationTargetException, ClassNotFoundException {
        InputStream resourceAsStream = Resources.getResourceAsStream("sqlMapConfig.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().buid(resourceAsStream);
        SqlSession sqlSession = sqlSessionFactory.openSession();


        IUserDao userDao = sqlSession.getMapper(IUserDao.class);

        List<User> list = userDao.findList();
        System.out.println(list);
    }

    @Test
    public void insertTest() throws PropertyVetoException, DocumentException, IllegalAccessException, IntrospectionException, InstantiationException, NoSuchFieldException, SQLException, InvocationTargetException, ClassNotFoundException {
        InputStream resourceAsStream = Resources.getResourceAsStream("sqlMapConfig.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().buid(resourceAsStream);
        SqlSession sqlSession = sqlSessionFactory.openSession();


        IUserDao userDao = sqlSession.getMapper(IUserDao.class);

        User user = new User();
        user.setId(4);
        user.setUsername("lagou-test");
        userDao.insertUser(user);
//        sqlSession.commit();
    }

    @Test
    public void updateTest() throws PropertyVetoException, DocumentException, IllegalAccessException, IntrospectionException, InstantiationException, NoSuchFieldException, SQLException, InvocationTargetException, ClassNotFoundException {
        InputStream resourceAsStream = Resources.getResourceAsStream("sqlMapConfig.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().buid(resourceAsStream);
        SqlSession sqlSession = sqlSessionFactory.openSession();


        IUserDao userDao = sqlSession.getMapper(IUserDao.class);

        User user = new User();
        user.setId(4);
        user.setUsername("lagou-test-4");
        userDao.updateUser(user);
//        sqlSession.commit();
    }

    @Test
    public void deleteTest() throws PropertyVetoException, DocumentException, IllegalAccessException, IntrospectionException, InstantiationException, NoSuchFieldException, SQLException, InvocationTargetException, ClassNotFoundException {
        InputStream resourceAsStream = Resources.getResourceAsStream("sqlMapConfig.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().buid(resourceAsStream);
        SqlSession sqlSession = sqlSessionFactory.openSession();


        IUserDao userDao = sqlSession.getMapper(IUserDao.class);

        userDao.deleteUserById(4);
//        sqlSession.commit();
    }
}
