import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import com.test.jwj.underMoon.DataBase.ContributesDao;
import com.test.jwj.underMoon.DataBase.UserDao;
import com.test.jwj.underMoon.bean.TranObject;
import com.test.jwj.underMoon.bean.User;
import com.test.jwj.underMoon.global.Result;


public class addEnlistTest {

	@Test
	public void test() {
		TranObject tran = new TranObject();
		tran.setSendId(1);
		tran.setObject(5);
		Result res = UserDao.updateRegist(tran);
		System.out.println(res);
	}

}
