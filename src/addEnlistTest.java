import java.io.File;
import java.io.IOException;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import org.junit.Test;

import com.google.gson.Gson;
import com.test.jwj.underMoon.bean.MeetingDetail;


public class addEnlistTest {

	@Test
	public void test() {
		MeetingDetail detail = new MeetingDetail();
		detail.setId(2);
		detail.setCity("shh");
		detail.setContent("content1");
		detail.setType("匀称");
		detail.setLoveType("要求1");
		detail.setAge(25);
		detail.setMarry(0);
		detail.setHeight(193);
		detail.setJob("coder");
		detail.setFigure("匀称");
		detail.setXingzuo("天秤");
		detail.score = 10;
		File file = new File("D:\\images" + File.separator + "1" + File.separator + "1.jpg");
		String imageType = "multipart/form-data";
		Gson gson = new Gson();
		String meetingDetailJson = gson.toJson(detail);
		RequestBody requestBody = new MultipartBody.Builder()
			.setType(MultipartBody.FORM)
			.addFormDataPart("meetingDetail", meetingDetailJson)
			.addFormDataPart("imagetype", imageType)
			.addFormDataPart("file", file.getName(), RequestBody.create(MediaType.parse("image/png"), file))
			.addFormDataPart("file", file.getName(), RequestBody.create(MediaType.parse("image/png"), file))
			.build();
		String url = "http://127.0.0.1:8080/qiqiim-server/createmeeting";
		final Request request = new Request.Builder()
			.url(url)
			.post(requestBody)
			.build();
		OkHttpClient client = new OkHttpClient();
		try {
			Response response = client.newCall(request).execute();
//			TranObject tran = new Gson().fromJson(response.body().string(), TranObject.class);
			System.out.println(response.body().string());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
//		new OkHttpClient().newCall(request).enqueue(new Callback(){
//			@Override
//			public void onFailure(Call arg0, IOException arg1) {
//				System.out.println("failure");
//			}
//			
//			@Override
//			public void onResponse(Call arg0, Response arg1) throws IOException {
//				System.out.println("success");
//			}
//		});
	}
}
