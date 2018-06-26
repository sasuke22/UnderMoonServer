import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import com.test.jwj.underMoon.DataBase.ContributesDao;
import com.test.jwj.underMoon.DataBase.FriendDao;
import com.test.jwj.underMoon.DataBase.UserDao;
import com.test.jwj.underMoon.bean.MeetingDetail;
import com.test.jwj.underMoon.bean.TranObject;
import com.test.jwj.underMoon.bean.User;
import com.test.jwj.underMoon.client.ClientActivity;
import com.test.jwj.underMoon.global.Result;


public class addEnlistTest {

	@Test
	public void test() {
		ArrayList<MeetingDetail> list = ContributesDao.selectContrbutesById(2);
		for (MeetingDetail meetingDetail : list) {
			System.out.println("detial " + meetingDetail.toString());
		}
	}

}
