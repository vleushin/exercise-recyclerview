package in.leush.exercise.recyclerview.ui.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import in.leush.exercise.recyclerview.R;
import in.leush.exercise.recyclerview.db.Contract;
import in.leush.exercise.recyclerview.ui.ACursorRecyclerViewAdapter;

public class MessagesAdapter extends ACursorRecyclerViewAdapter<RecyclerView.ViewHolder> {
    private final LayoutInflater mLayoutInflater;

    public MessagesAdapter(Context context) {
        super(null);
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, Cursor cursor) {
        int columnMessageIndex = cursor.getColumnIndex(Contract.MessagesEntry.COLUMN_NAME_MESSAGE);
        String message = cursor.getString(columnMessageIndex);

        MessageViewHolder messageViewHolder = (MessageViewHolder) holder;
        messageViewHolder.textView.setText(message);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.list_item_message, parent, false);
        return new MessageViewHolder(view);
    }

    static final class MessageViewHolder extends RecyclerView.ViewHolder {

        TextView textView;

        private MessageViewHolder(View itemView) {
            super(itemView);
            this.textView = (TextView) itemView.findViewById(R.id.text_view);
        }
    }
}
