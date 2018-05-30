import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import com.test.jwj.underMoon.DataBase.ContributesDao;
import com.test.jwj.underMoon.DataBase.UserDao;
import com.test.jwj.underMoon.bean.TranObject;
import com.test.jwj.underMoon.bean.User;
import com.test.jwj.underMoon.client.ClientActivity;
import com.test.jwj.underMoon.global.Result;


public class addEnlistTest {

	@Test
	public void test() {
		TranObject tran = new TranObject();
		tran.setObject(1);
		ArrayList<String> registRes = ContributesDao.queryRegistName(tran);
		
		System.out.println(registRes.toString());
	}

}
