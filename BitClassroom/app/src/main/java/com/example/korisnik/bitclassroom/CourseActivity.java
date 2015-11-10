package com.example.korisnik.bitclassroom;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.korisnik.bitclassroom.helpers.ConnectHelper;
import com.example.korisnik.bitclassroom.models.Course;
import com.example.korisnik.bitclassroom.models.Post;
import com.example.korisnik.bitclassroom.models.User;

import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.UUID;

/**
 * Created by Korisnik on 10/29/2015.
 */
public class CourseActivity extends AppCompatActivity {

    private static List<Post> posts;
    private static Course currentCourse;
    private TextView mCourseName;
    private TextView mCourseDescription;
    private TextView mCourseTeacher;
    private Button mCourseScheduleButton;
    private ImageView mCourseImage;
    private PostView postView;
    private PostAdapter postAdapter;
    private RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_view);

        mCourseImage = (ImageView) findViewById(R.id.course_picture);
        new LoadImage(mCourseImage).execute(currentCourse.getPicURL());
        mCourseName = (TextView) findViewById(R.id.course_name);
        mCourseDescription = (TextView) findViewById(R.id.course_description);
        mCourseTeacher = (TextView) findViewById(R.id.teacher_name);
        mCourseScheduleButton = (Button) findViewById(R.id.course_schedule_btn);
        mCourseScheduleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = CourseScheduleActivity.newIntent(getApplicationContext(),currentCourse.getWebId());
                startActivity(i);
            }
        });

        mCourseName.setText(currentCourse.getName());
        mCourseDescription.setText(currentCourse.getDescription());
        mCourseTeacher.setText(currentCourse.getTeacher());

        recyclerView = (RecyclerView) findViewById(R.id.post_recycler);
        postAdapter = new PostAdapter(posts);
        recyclerView.setAdapter(postAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public static Intent newIntent(Context packageContext,Course course,List<Post>postList) {
        Intent i = new Intent(packageContext, CourseActivity.class);
        currentCourse = course;
        posts = postList;
        return i;
    }

    private class PostView extends RecyclerView.ViewHolder{
        private Post mPost;
        private TextView nameText;
        private TextView contentText;
        private TextView timestampText;
        private TextView duedateText;
        private TextView userText;
        private int mPosition;

        public PostView(View itemView) {
            super(itemView);

            nameText = (TextView) itemView.findViewById(R.id.post_fragment_post_name);
            contentText = (TextView) itemView.findViewById(R.id.post_fragment_post_content);
            timestampText = (TextView) itemView.findViewById(R.id.post_fragment_post_timestamp);
            duedateText = (TextView) itemView.findViewById(R.id.post_fragment_post_due_date);
            userText = (TextView) itemView.findViewById(R.id.post_fragment_post_user);

        }

        public void bindPost(Post post,int position){
            mPost = post;
            nameText.setText(post.getName());
            contentText.setText(post.getContent());
            timestampText.setText(post.getTimeStamp());
            if(!post.getDueDate().equals("null null")){
            duedateText.setText("Due: "+post.getDueDate());
            }else{
                duedateText.setText(" ");
            }
            userText.setText(post.getUser());
            mPosition = position;
        }
    }

    private class PostAdapter extends RecyclerView.Adapter<PostView>{

        private List<Post> postList;

        public PostAdapter(List<Post> postList){
            this.postList = postList;
        }


        @Override
        public PostView onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(CourseActivity.this);

            View view = layoutInflater.inflate(R.layout.post_fragment, parent, false);
            return new PostView(view);
        }

        @Override
        public void onBindViewHolder(PostView holder, int position) {
            final Post post = posts.get(position);
            holder.bindPost(post, position);
        }


        @Override
        public int getItemCount() {
            return posts.size();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ConnectHelper.postTimestamps.clear();
        ConnectHelper.postUsers.clear();
        ConnectHelper.postContents.clear();
        ConnectHelper.postNames.clear();
        ConnectHelper.postDueDates.clear();
        ConnectHelper.posts.clear();


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
                bitmap = BitmapFactory.decodeStream((InputStream) new URL(args[0]).getContent());

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

                Intent i = LogInActivity.newIntent(CourseActivity.this);
                startActivityForResult(i, 0);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
