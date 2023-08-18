package volovyk.thenullpointer.ui

import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import volovyk.thenullpointer.R
import volovyk.thenullpointer.data.entity.UploadedFile
import volovyk.thenullpointer.databinding.FragmentFileListItemBinding
import java.util.Date
import java.util.concurrent.TimeUnit

class FileListAdapter(
    private val onItemClick: (UploadedFile) -> Unit,
    private val onItemLongClick: (UploadedFile) -> Unit,
    private val onItemShareButtonClick: (UploadedFile) -> Unit
) : ListAdapter<UploadedFile, FileListAdapter.UploadedFileViewHolder>(UploadedFileDiffCallBack) {

    class UploadedFileViewHolder(
        private val binding: FragmentFileListItemBinding,
        private val onItemClick: (UploadedFile) -> Unit,
        private val onItemLongClick: (UploadedFile) -> Unit,
        private val onItemShareButtonClick: (UploadedFile) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(uploadedFile: UploadedFile) {
            val context = itemView.context
            binding.apply {
                fileNameTextView.text = uploadedFile.name
                uploadedAtTextView.text =
                    context.getString(
                        R.string.uploaded_at,
                        DateFormat.getDateFormat(context).format(uploadedFile.uploadedAt)
                    )
                expiresInTextView.text = context.getString(
                    R.string.expires_in,
                    getDaysDifference(uploadedFile.expiresAt).toString()
                )
                cardView.setOnClickListener { onItemClick(uploadedFile) }
                cardView.setOnLongClickListener {
                    onItemLongClick(uploadedFile)
                    true
                }
                shareImageButton.setOnClickListener { onItemShareButtonClick(uploadedFile) }
            }
        }
    }

    private object UploadedFileDiffCallBack : DiffUtil.ItemCallback<UploadedFile>() {
        override fun areItemsTheSame(oldItem: UploadedFile, newItem: UploadedFile): Boolean =
            oldItem.token == newItem.token

        override fun areContentsTheSame(oldItem: UploadedFile, newItem: UploadedFile): Boolean =
            oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UploadedFileViewHolder =
        UploadedFileViewHolder(
            FragmentFileListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), onItemClick, onItemLongClick, onItemShareButtonClick
        )

    override fun onBindViewHolder(holder: UploadedFileViewHolder, position: Int) =
        holder.bind(getItem(position))

    companion object {
        private fun getDaysDifference(date: Date): Long {
            // Get the current date and time
            val currentDate = Date()

            // Calculate the time difference in milliseconds
            val timeDifferenceInMillis = date.time - currentDate.time

            // Convert the time difference from milliseconds to days
            return TimeUnit.MILLISECONDS.toDays(timeDifferenceInMillis)
        }
    }
}
