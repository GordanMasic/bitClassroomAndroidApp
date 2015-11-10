package com.example.korisnik.bitclassroom;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.korisnik.bitclassroom.helpers.ConnectHelper;
import com.example.korisnik.bitclassroom.helpers.ServiceRequest;
import com.example.korisnik.bitclassroom.models.Course;
import com.example.korisnik.bitclassroom.models.User;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

public class CourseFeedActivity extends AppCompatActivity {

    private static User currentUser;
    public static List<Course> courses;
    private CourseView courseView;
    private CourseAdapter courseAdapter;
    private RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_feed);

        recyclerView = (RecyclerView) findViewById(R.id.course_recycler_view);
        courseAdapter = new CourseAdapter(courses);
        recyclerView.setAdapter(courseAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    public static Intent newIntent(Context packageContext, User user, List<Course> coursesList) {
        Intent i = new Intent(packageContext, CourseFeedActivity.class);
        currentUser = user;
        courses =
                coursesList;
        return i;
    }


    private class CourseView extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Course mCourse;
        private  ImageView mCourseImage;
        private TextView nameText;
        private TextView descriptionText;
        private TextView teacherText;
        private int mPosition;

        public CourseView(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            nameText = (TextView) itemView.findViewById(R.id.course_name);
            descriptionText = (TextView) itemView.findViewById(R.id.course_description);
            teacherText = (TextView) itemView.findViewById(R.id.teacher_name);
            mCourseImage = (ImageView) itemView.findViewById(R.id.course_picture);
        }

        @Override
        public void onClick(View v) {
            ConnectHelper.getPosts(CourseFeedActivity.this, mCourse);
        }

        public void bindCourse(Course course, int position) {
            mCourse = course;

            new LoadImage(mCourseImage).execute(mCourse.getPicURL());
            nameText.setText(mCourse.getName());
            descriptionText.setText(mCourse.getDescription());
            teacherText.setText(mCourse.getTeacher());
            mPosition = position;
        }
    }

    private class CourseAdapter extends RecyclerView.Adapter<CourseView> {

        private List<Course> courseList;

        public CourseAdapter(List<Course> courseList) {
            this.courseList = courseList;
        }


        @Override
        public CourseView onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(CourseFeedActivity.this);

            View view = layoutInflater.inflate(R.layout.course_fragment, parent, false);
            return new CourseView(view);
        }

        @Override
        public void onBindViewHolder(CourseView holder, int position) {
            final Course course = courseList.get(position);
            holder.bindCourse(course, position);
        }


        @Override
        public int getItemCount() {
            return courseList.size();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ConnectHelper.courses.clear();
        ConnectHelper.coursePictures.clear();
        ConnectHelper.courseNames.clear();
        ConnectHelper.courseTeachers.clear();
        ConnectHelper.courseDescriptions.clear();
        ConnectHelper.courseWebIds.clear();
    }

    private class LoadImage extends AsyncTask<String, String, Bitmap> {
        private ImageView img;
        public LoadImage(ImageView imgView){
            img = imgView;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        protected Bitmap doInBackground(String... args) {
            Bitmap bitmap = null;
            try {
                bitmap = BitmapFactory.decodeStream((InputStream)new URL(args[0]).getContent());

            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        protected void onPostExecute(Bitmap image) {

                img.setImageBitmap(image);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar_items,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_item_logout:
                ConnectHelper.coursePictures.clear();
                ConnectHelper.courseWebIds.clear();
                ConnectHelper.courseNames.clear();
                ConnectHelper.courseDescriptions.clear();
                ConnectHelper.courseTeachers.clear();
                ConnectHelper.courses.clear();

                ConnectHelper.posts.clear();
                ConnectHelper.postContents.clear();
                ConnectHelper.postDueDates.clear();
                ConnectHelper.postNames.clear();
                ConnectHelper.postTimestamps.clear();
                ConnectHelper.postUsers.clear();

                ConnectHelper.students.clear();
                ConnectHelper.studentPics.clear();
                ConnectHelper.studentTokens.clear();
                ConnectHelper.studentRoles.clear();
                ConnectHelper.studentNames.clear();
                ConnectHelper.studentLastnames.clear();
                ConnectHelper.studentIds.clear();

                SharedPreferences prefs = getSharedPreferences("ba.classroom",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.remove("email");
                editor.remove("password");
                editor.remove("user_id");
                editor.commit();

                Intent i = LogInActivity.newIntent(CourseFeedActivity.this);
                startActivityForResult(i, 0);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
