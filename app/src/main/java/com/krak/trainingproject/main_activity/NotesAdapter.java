package com.krak.trainingproject.main_activity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.krak.trainingproject.R;
import com.krak.trainingproject.new_note_activity.NewNoteActivity;
import com.krak.trainingproject.tools.Session;

import java.util.ArrayList;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteHolder> {

    private ArrayList<Note> notes;
    private LayoutInflater inflater;
    private Context context;

    public NotesAdapter(Context context, ArrayList<Note> notes) {
        this.notes = notes;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public NoteHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.note, parent, false);
        return new NoteHolder(view);
    }

    @Override
    public void onBindViewHolder(NotesAdapter.NoteHolder holder, int position) {
        Note note = notes.get(position);
        holder.init(note);
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    class NoteHolder extends RecyclerView.ViewHolder{

        private ImageView image;
        private TextView text;
        private TextView masterName;
        private AppCompatButton changeNoteBtn;
        private Note note;

        public NoteHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.noteProfileImage);
            text = itemView.findViewById(R.id.noteText);
            masterName = itemView.findViewById(R.id.nameInNote);
            changeNoteBtn = itemView.findViewById(R.id.changeNoteBtn);
        }

        private void addOnClickListener(){
            changeNoteBtn.setOnClickListener(view -> {
                Intent intent = new Intent(context, NewNoteActivity.class);
                intent.putExtra(NewNoteActivity.IS_UPDATE, true);
                intent.putExtra(NewNoteActivity.NOTE, note.toString());
                context.startActivity(intent);
            });
        }

        public void init(Note note) {
            this.note = note;
            masterName.setText(note.getMasterName());
            text.setText(note.getText());
            if (Session.getUser().getEmail().equals(note.getEmail())) {
                addOnClickListener();
            } else {
                changeNoteBtn.setVisibility(View.GONE);
            }
        }
    }
}
