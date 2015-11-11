package com.example.korisnik.bitclassroom.helpers;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;

import com.example.korisnik.bitclassroom.CourseActivity;
import com.example.korisnik.bitclassroom.CourseFeedActivity;
import com.example.korisnik.bitclassroom.LogInActivity;
import com.example.korisnik.bitclassroom.MessageActivity;
import com.example.korisnik.bitclassroom.StudentFeedActivity;
import com.example.korisnik.bitclassroom.WelcomeActivity;
import com.example.korisnik.bitclassroom.models.Course;
import com.example.korisnik.bitclassroom.models.Post;
import com.example.korisnik.bitclassroom.models.User;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * This class is created for connecting to web application restful service. Methods in this class
 * are used for exchanging Json objects between restful services and android application.
 */
public class ConnectHelper {

    //Lists for extracting courses from Json object.
    public static List<String> courseNames = new ArrayList<>();
    public static List<String> courseDescriptions = new ArrayList<>();
    public static List<String> courseTeachers = new ArrayList<>();
    public static List<String> courseWebIds = new ArrayList<>();
    public static List<String> coursePictures = new ArrayList<>();
    public static List<Course> courses = new ArrayList<>();

    //Lists for extracting students from Json object.
    public static List<String> studentNames = new ArrayList<>();
    public static List<String> studentLastnames = new ArrayList<>();
    public static List<String> studentTokens = new ArrayList<>();
    public static List<String> studentRoles = new ArrayList<>();
    public static List<String> studentIds = new ArrayList<>();
    public static List<String> studentPics = new ArrayList<>();
    public static List<User> students = new ArrayList<>();

    //Lists for extracting posts from Json object.
    public static List<String> postNames = new ArrayList<>();
    public static List<String> postDueDates = new ArrayList<>();
    public static List<String> postTimestamps = new ArrayList<>();
    public static List<String> postUsers = new ArrayList<>();
    public static List<String> postContents = new ArrayList<>();
    public static List<Post> posts = new ArrayList<>();

    //Default variables.
    private static User user;
    private static String url;
    private static AppCompatActivity currentActivity;
    private static Course currentCourse;


    /**
     * Method for signing into application. Method receives Json object that contains data
     * about user that is signed into application. In case that user doesn't have permission
     * to enter the application it will return the log in activity, otherwise depending on
     * which role has user it will start new activity.
     *
     * @param activity - Current activity in which this method is called.
     * @param currentURL - URL that method will send new request.
     * @return - Other activity if signing in was successful or current activity in case of failure.
     */
    public static Callback loginCheck(final AppCompatActivity activity, String currentURL) {
        currentActivity = activity;
        url = currentURL;
        return new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                e.printStackTrace();
                Intent main = LogInActivity.newIntent(currentActivity.getApplicationContext());
                currentActivity.startActivity(main);
            }

            @Override
            public void onResponse(Response response) throws IOException {

                String responseJSON = response.body().string();
                JSONObject obj;
                try {

                    //Extracting data from Json object.
                    obj = new JSONObject(responseJSON);
                    String name = obj.getString("name");
                    String lastname = obj.getString("lastname");
                    String role = obj.getString("role");
                    String token = obj.getString("token");
                    String webId = obj.getString("user_id");
                    user = new User(UUID.randomUUID(), name, lastname, role, token, webId,"");
                    user.setId(UUID.randomUUID());

                    //Creating new Json object that contains user token.
                    JSONObject json = new JSONObject();
                    try {
                        json.put("token", user.getToken());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    //Saving user token in memory.
                    SharedPreferences.Editor editor = WelcomeActivity.prefs.edit();
                    editor.putString("user_token",user.getToken());
                    editor.commit();

                    //Depending on the role of user sending new request on REST services and starting new activity.
                    if (role.equals("Student")) {
                        ServiceRequest.post(url + "api/courses", json.toString(), getCourses(currentActivity));
                    } else {
                        ServiceRequest.post(url + "api/students", json.toString(), getStudents(currentActivity));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    /**
     * Method that sends new request to REST service to ask for all courses for one student.
     *
     * @param user - Student whose courses are requested.
     * @param activity - Current activity that calls this method.
     */
    public static void getStudentCourses(User user, AppCompatActivity activity) {
        JSONObject json = new JSONObject();
        try {
            json.put("token", user.getToken());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ServiceRequest.post(url + "api/courses", json.toString(), getCourses(currentActivity));

    }

    /**
     * Method that receives an Json array and extracts all courses and course data from it.
     *
     * @param activity - Current activity that calls this method.
     * @return - CourseFeedActivity with all courses from one student.
     */
    private static Callback getCourses(AppCompatActivity activity) {
        currentActivity = activity;
        return new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Response response) throws IOException {

                String responseJSON = response.body().string();
                JSONArray jsonArray;
                try {

                    //Getting names for each course.
                    jsonArray = new JSONArray(responseJSON);
                    JSONObject object = jsonArray.getJSONObject(0);
                    JSONArray arr = object.getJSONArray("names");
                    for (int i = 0; i < arr.length(); i++) {
                        courseNames.add(String.valueOf(arr.get(i)));
                    }

                    //Getting description from each course.
                    JSONObject object1 = jsonArray.getJSONObject(1);
                    JSONArray arr1 = object1.getJSONArray("descriptions");
                    for (int i = 0; i < arr1.length(); i++) {
                        courseDescriptions.add(String.valueOf(arr1.get(i)));
                    }

                    //Getting teacher name and last name from each course.+
                    JSONObject object2 = jsonArray.getJSONObject(2);
                    JSONArray arr2 = object2.getJSONArray("teachers");
                    for (int i = 0; i < arr2.length(); i++) {
                        courseTeachers.add(String.valueOf(arr2.get(i)));
                    }

                    //Getting id from each course that is used in web application.
                    JSONObject object3 = jsonArray.getJSONObject(3);
                    JSONArray arr3 = object3.getJSONArray("course_id");
                    for (int i = 0; i < arr3.length(); i++) {
                        courseWebIds.add(String.valueOf(arr3.get(i)));
                    }

                    //Getting image from each course.
                    JSONObject object4 = jsonArray.getJSONObject(4);
                    JSONArray arr4 = object4.getJSONArray("course_pic");
                    for (int i = 0; i < arr4.length(); i++) {
                        coursePictures.add(String.valueOf(arr4.get(i)));
                    }

                    //Creating all courses.
                    for (int i = 0; i < courseNames.size(); i++) {
                        courses.add(new Course(UUID.randomUUID(), courseNames.get(i), courseDescriptions.get(i), courseTeachers.get(i), courseWebIds.get(i),coursePictures.get(i)));
                    }

                    //Starting new activity.
                    Intent main = CourseFeedActivity.newIntent(currentActivity.getApplicationContext(), user, courses);
                    currentActivity.startActivity(main);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };
    }

    /**
     * Method that receives one Json array that contains data about all students assigned to one mentor.
     *
     * @param activity - Current activity that calls this method.
     * @return - StudentFeedActivity
     */
    private static Callback getStudents(AppCompatActivity activity) {
        currentActivity = activity;
        return new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Response response) throws IOException {

                String responseJSON = response.body().string();
                JSONArray jsonArray;
                try {

                    //Getting names for each student.
                    jsonArray = new JSONArray(responseJSON);
                    JSONObject object = jsonArray.getJSONObject(0);
                    JSONArray arr = object.getJSONArray("names");
                    for (int i = 0; i < arr.length(); i++) {
                        studentNames.add(String.valueOf(arr.get(i)));
                    }

                    //Getting last names for each student.
                    JSONObject object1 = jsonArray.getJSONObject(1);
                    JSONArray arr1 = object1.getJSONArray("lastnames");
                    for (int i = 0; i < arr1.length(); i++) {
                        studentLastnames.add(String.valueOf(arr1.get(i)));
                    }

                    //Getting tokens for each student.
                    JSONObject object2 = jsonArray.getJSONObject(3);
                    JSONArray arr2 = object2.getJSONArray("tokens");
                    for (int i = 0; i < arr2.length(); i++) {
                        studentTokens.add(String.valueOf(arr2.get(i)));
                    }

                    //Getting roles for each student.
                    JSONObject object3 = jsonArray.getJSONObject(2);
                    JSONArray arr3 = object3.getJSONArray("roles");
                    for (int i = 0; i < arr3.length(); i++) {
                        studentRoles.add(String.valueOf(arr3.get(i)));
                    }

                    //Getting id for each student.
                    JSONObject object4 = jsonArray.getJSONObject(4);
                    JSONArray arr4 = object4.getJSONArray("user_id");
                    for (int i = 0; i < arr4.length(); i++) {
                        studentIds.add(String.valueOf(arr4.get(i)));
                    }

                    //Getting image for each student.
                    JSONObject object5 = jsonArray.getJSONObject(5);
                    JSONArray arr5 = object5.getJSONArray("user_pic");
                    for (int i = 0; i < arr5.length(); i++) {
                        studentPics.add(String.valueOf(arr5.get(i)));
                    }

                    //Creating each student.
                    for (int i = 0; i < studentNames.size(); i++) {
                        students.add(new User(UUID.randomUUID(), studentNames.get(i), studentLastnames.get(i), studentRoles.get(i), studentTokens.get(i), studentIds.get(i),studentPics.get(i)));
                    }

                    //Starting new activity.
                    Intent main = StudentFeedActivity.newIntent(currentActivity.getApplicationContext(), students);
                    currentActivity.startActivity(main);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };
    }


    /**
     * Method that sends new request to REST service to ask for all posts for one course.
     *
     * @param appCompatActivity - Current activity that calls this method.
     * @param course - Course whose posts are required.
     */
    public static void getPosts(AppCompatActivity appCompatActivity, Course course) {
        JSONObject json = new JSONObject();
        try {
            json.put("course_id", course.getWebId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ServiceRequest.post(url + "api/posts", json.toString(), receivePosts(appCompatActivity, course));
    }

    /**
     * Method that receives one Json array that contains data about all posts from one course.
     *
     * @param appCompatActivity - Current activity that calls this method.
     * @param course - Course whose posts are required.
     * @return CourseActivity.
     */
    public static Callback receivePosts(AppCompatActivity appCompatActivity, final Course course) {
        currentActivity = appCompatActivity;
        currentCourse = course;
        return new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Response response) throws IOException {

                String responseJSON = response.body().string();
                JSONArray jsonArray;
                try {

                    //Getting names for each post.
                    jsonArray = new JSONArray(responseJSON);
                    JSONObject object = jsonArray.getJSONObject(0);
                    JSONArray arr = object.getJSONArray("names");
                    for (int i = 0; i < arr.length(); i++) {
                        postNames.add(String.valueOf(arr.get(i)));
                    }

                    //Getting content of each post.
                    JSONObject object1 = jsonArray.getJSONObject(1);
                    JSONArray arr1 = object1.getJSONArray("contents");
                    for (int i = 0; i < arr1.length(); i++) {
                        postContents.add(String.valueOf(arr1.get(i)));
                    }

                    //Getting time stamp for each post.
                    JSONObject object2 = jsonArray.getJSONObject(2);
                    JSONArray arr2 = object2.getJSONArray("timestamps");
                    for (int i = 0; i < arr2.length(); i++) {
                        postTimestamps.add(String.valueOf(arr2.get(i)));
                    }

                    //Getting due date for each post.
                    JSONObject object3 = jsonArray.getJSONObject(3);
                    JSONArray arr3 = object3.getJSONArray("duedates");
                    for (int i = 0; i < arr3.length(); i++) {
                        postDueDates.add(String.valueOf(arr3.get(i)));
                    }

                    //Getting user for each post.
                    JSONObject object4 = jsonArray.getJSONObject(4);
                    JSONArray arr4 = object4.getJSONArray("users");
                    for (int i = 0; i < arr4.length(); i++) {
                        postUsers.add(String.valueOf(arr4.get(i)));
                    }

                    //Creating each post.
                    for (int i = 0; i < postNames.size(); i++) {
                        posts.add(new Post(UUID.randomUUID(), postNames.get(i), postContents.get(i), postTimestamps.get(i), postDueDates.get(i), postUsers.get(i)));
                    }

                    //Starting new activity.
                    Intent main = CourseActivity.newIntent(currentActivity
                            .getApplicationContext(), currentCourse, posts);
                    currentActivity.startActivity(main);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };
    }

    /**
     * Method that is used for sending message from mentor to student.
     *
     * @param messageSubject - Message subject.
     * @param messageContent - Message content.
     * @param activity - Current activity that calls this method.
     */
    public static void sendMessage(String messageSubject,String messageContent,AppCompatActivity activity){
        String senderId = WelcomeActivity.prefs.getString("user_id",null);
        JSONObject json = new JSONObject();
        try{
            json.put("subject",messageSubject);
            json.put("message",messageContent);
            json.put("receiver",user.getWebId());
            json.put("sender",senderId);

        }catch (JSONException e){
            e.printStackTrace();
        }
        ServiceRequest.post(LogInActivity.url + "api/send_message", json.toString(), receiveMessageResponse(activity));
    }


    /**
     * Method that receives response if message is sent or not.
     * 
     * @param appCompatActivity - Current activity that calls this method.
     * @return StudentFeedActivity.
     */
    public static Callback receiveMessageResponse(AppCompatActivity appCompatActivity) {
        currentActivity = appCompatActivity;
        return new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Response response) throws IOException {

                String responseJSON = response.body().string();
                JSONObject obj;
                try {
                    obj = new JSONObject(responseJSON);
                    String response1 = obj.getString("response");

                    if(response.equals("Ok")) {
                        Intent main = StudentFeedActivity.newIntent(currentActivity
                                .getApplicationContext(),students);
                        currentActivity.startActivity(main);
                    }else{
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };
    }

    /**
     * Method that clears all static lists in this class
     */
    public static void clearAll(){

        students.clear();
        studentNames.clear();
        studentLastnames.clear();
        studentTokens.clear();
        studentRoles.clear();
        studentPics.clear();
        students.clear();
        studentIds.clear();

        courses.clear();
        coursePictures.clear();
        courseWebIds.clear();
        courseNames.clear();
        courseDescriptions.clear();
        courseTeachers.clear();

        postContents.clear();
        postDueDates.clear();
        postNames.clear();
        postTimestamps.clear();
        posts.clear();
        postUsers.clear();
    }
}
