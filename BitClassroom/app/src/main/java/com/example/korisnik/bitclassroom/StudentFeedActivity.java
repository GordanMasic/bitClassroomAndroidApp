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
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.korisnik.bitclassroom.helpers.ConnectHelper;
import com.example.korisnik.bitclassroom.models.Course;
import com.example.korisnik.bitclassroom.models.User;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

/**
 * Created by gordan.masic on 29/10/15.
 */
public class StudentFeedActivity extends AppCompatActivity {

    public static List<User> users;
    private StudentView studentView;
    private StudentAdapter studentAdapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_students_mentor_feed);

        recyclerView = (RecyclerView) findViewById(R.id.students_recycler_view);
        studentAdapter = new StudentAdapter(users);
        recyclerView.setAdapter(studentAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public static Intent newIntent(Context packageContext, List<User> usersList) {
        Intent i = new Intent(packageContext, StudentFeedActivity.class);
        users = usersList;
        return i;
    }

    private class StudentView extends RecyclerView.ViewHolder implements View.OnClickListener{
        private User mUser;
        private TextView nameText;
        private TextView lastnameText;
        private Button sendMessage;
        private ImageView image;
        private int mPosition;

        public StudentView(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            image = (ImageView) itemView.findViewById(R.id.student_picture);
            nameText = (TextView) itemView.findViewById(R.id.student_fragment_student_name);
            lastnameText = (TextView) itemView.findViewById(R.id.student_fragment_student_lastname);
            sendMessage = (Button) itemView.findViewById(R.id.send_message_button);

        }

        @Override
        public void onClick(View v) {
            ConnectHelper.getStudentCourses(mUser, StudentFeedActivity.this);

        }

        public void bindCourse(User user,int position){
            mUser = user;
            nameText.setText(user.getName());
            lastnameText.setText(user.getLastname());
            new LoadImage(image).execute(user.getUserPic());
            sendMessage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = MessageActivity.newIntent(getApplicationContext(),mUser);
                    startActivity(intent);
                }
            });
            mPosition = position;
        }
    }

    private class StudentAdapter extends RecyclerView.Adapter<StudentView>{

        private List<User> userList;
        private TextView userName;
        private TextView userLastname;

        public StudentAdapter(List<User> userList){
            this.userList = userList;
        }


        @Override
        public StudentView onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(StudentFeedActivity.this);

            View view = layoutInflater.inflate(R.layout.student_fragment, parent, false);
            return new StudentView(view);
        }

        @Override
        public void onBindViewHolder(StudentView holder, int position) {
            final User student = users.get(position);
            holder.bindCourse(student,position);
        }


        @Override
        public int getItemCount() {
            return userList.size();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ConnectHelper.studentIds.clear();
        ConnectHelper.studentLastnames.clear();
        ConnectHelper.studentNames.clear();
        ConnectHelper.studentRoles.clear();
        ConnectHelper.studentTokens.clear();
        ConnectHelper.students.clear();
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

                Intent i = LogInActivity.newIntent(StudentFeedActivity.this);
                startActivityForResult(i, 0);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
