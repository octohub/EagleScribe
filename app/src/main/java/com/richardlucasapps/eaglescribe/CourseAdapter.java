package com.richardlucasapps.eaglescribe;

// Adding commit to ensure EagleScribe is in https://archiveprogram.github.com/.

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.richardlucasapps.eaglescribe.Utils.ClassStatus;
import com.richardlucasapps.eaglescribe.model.Course;
import com.richardlucasapps.eaglescribe.screens.base.CoursePresenter;
import java.util.ArrayList;
import java.util.List;

import static com.richardlucasapps.eaglescribe.Utils.CLASS_CANCELLED;
import static com.richardlucasapps.eaglescribe.Utils.CLASS_CLOSED_USER_NOT_SUBSCRIBED;
import static com.richardlucasapps.eaglescribe.Utils.CLASS_CLOSED_USER_SUBSCRIBED;
import static com.richardlucasapps.eaglescribe.Utils.CLASS_OPEN;
import static com.richardlucasapps.eaglescribe.Utils.CLASS_OPEN_USER_SUBSCRIBED;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.ViewHolder> {

  private CoursePresenter presenter;
  private List<Course> courses;

  private List<Integer> loadingCourses;

  public CourseAdapter(CoursePresenter presenter) {
    this.presenter = presenter;
    loadingCourses = new ArrayList<>();
  }

  @Override public CourseAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.course, parent, false);
    return new ViewHolder(view, presenter, courses);
  }

  @Override public void onBindViewHolder(ViewHolder holder, int position) {
    Context context = holder.courseName.getContext();
    Course course = courses.get(position);

    holder.courseName.setText(course.course);
    holder.professor.setText(course.professor);

    String time = (course.isCancelled) ? context.getString(R.string.course_cancelled) : course.time;
    holder.time.setText(context.getString(R.string.course_time_format, time));

    @ClassStatus int status = getItemViewType(position);

    final boolean isLoading = isLoading(position);

    switch (status) {
      case CLASS_CANCELLED: {
        holder.courseName.setTextColor(ContextCompat.getColor(context, R.color.color_closed));
        holder.imageView.setVisibility(View.GONE);
        break;
      }
      case CLASS_OPEN: {
        holder.courseName.setTextColor(ContextCompat.getColor(context, R.color.color_open));
        holder.imageView.setVisibility(View.GONE);
        break;
      }
      case CLASS_OPEN_USER_SUBSCRIBED: {
        holder.courseName.setTextColor(ContextCompat.getColor(context, R.color.color_open));
        holder.imageView.setImageDrawable(
            ActivityCompat.getDrawable(context, R.drawable.ic_notifications_active_black_24dp));
        setLoadingState(holder, isLoading);
        break;
      }
      case CLASS_CLOSED_USER_NOT_SUBSCRIBED: {
        holder.courseName.setTextColor(ContextCompat.getColor(context, R.color.color_closed));
        holder.imageView.setImageDrawable(
            ActivityCompat.getDrawable(context, R.drawable.ic_notifications_none_black_24dp));
        setLoadingState(holder, isLoading);
        break;
      }
      case CLASS_CLOSED_USER_SUBSCRIBED: {
        holder.courseName.setTextColor(ContextCompat.getColor(context, R.color.color_closed));
        holder.imageView.setImageDrawable(
            ActivityCompat.getDrawable(context, R.drawable.ic_notifications_active_black_24dp));
        setLoadingState(holder, isLoading);
        break;
      }
    }
  }

  private void setLoadingState(final ViewHolder holder, final boolean isLoading) {
    if (isLoading) {
      holder.imageView.setVisibility(View.GONE);
      holder.progressBar.setVisibility(View.VISIBLE);
    } else {
      holder.imageView.setVisibility(View.VISIBLE);
      holder.progressBar.setVisibility(View.GONE);
    }
  }

  private boolean isLoading(int position) {
    return loadingCourses.contains(position);
  }

  @Override public int getItemCount() {
    if (courses != null) {
      return courses.size();
    } else {
      return 0;
    }
  }

  public void setList(List<Course> courses) {
    if (courses != null) {
      if (this.courses == null) {
        this.courses = new ArrayList<>();
      }
      this.courses.clear();
      this.courses.addAll(courses);
      notifyDataSetChanged();
    }
  }

  public ArrayList<Course> getCourses() {
    return (ArrayList<Course>) courses;
  }

  public void removeCourse(int position) {
    courses.remove(position);
    notifyItemRemoved(position);
  }

  @Nullable public Course getCourse(int adapterPosition) {
    if (courses != null) {
      return courses.get(adapterPosition);
    }
    return null;
  }

  @ClassStatus @Override public int getItemViewType(int position) {
    final Course course = courses.get(position);
    return Utils.getClassStatus(course);
  }

  public void showLoading(int adapterPosition) {
    loadingCourses.add(adapterPosition);
    notifyItemChanged(adapterPosition);
  }

  public void hideLoading(int adapterPosition) {
    loadingCourses.remove((Integer) adapterPosition);
    notifyItemChanged(adapterPosition);
  }

  static class ViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.time) public TextView time;
    @BindView(R.id.course_name) TextView courseName;
    @BindView(R.id.professor) TextView professor;
    @BindView(R.id.imageView) ImageView imageView;

    @BindView(R.id.progress_subscribe) ProgressBar progressBar;

    ViewHolder(View itemView, CoursePresenter presenter, List<Course> courses) {
      super(itemView);
      ButterKnife.bind(this, itemView);

      itemView.setOnClickListener(view -> {
        final Course course = courses.get(getAdapterPosition());
        presenter.clicked(course);
      });

      imageView.setOnClickListener(view -> {
        final int adapterPosition = getAdapterPosition();
        final Course course = courses.get(adapterPosition);
        presenter.subscribeClicked(course, adapterPosition);
      });
    }
  }
}
