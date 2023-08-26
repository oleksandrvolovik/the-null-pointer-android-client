package volovyk.thenullpointer.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import volovyk.thenullpointer.R
import volovyk.thenullpointer.data.remote.entity.FileUploadState
import volovyk.thenullpointer.databinding.FileUploadStateListItemBinding

class FileUploadStateListAdapter(private val onItemClick: (FileUploadState) -> Unit) :
    ListAdapter<FileUploadState, FileUploadStateListAdapter.FileUploadStateViewHolder>(
        FileUploadStateDiffCallback()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileUploadStateViewHolder =
        FileUploadStateViewHolder(
            FileUploadStateListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onItemClick
        )

    override fun onBindViewHolder(holder: FileUploadStateViewHolder, position: Int) =
        holder.bind(getItem(position))

    class FileUploadStateViewHolder(
        private val binding: FileUploadStateListItemBinding,
        private val onItemClick: (FileUploadState) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(fileUploadState: FileUploadState) {
            val context = itemView.context
            binding.apply {
                root.setOnClickListener { onItemClick(fileUploadState) }
                when (fileUploadState) {
                    is FileUploadState.InProgress -> {
                        uploadingFileNameTextView.text =
                            context.getString(R.string.uploading_file, fileUploadState.filename)
                        fileUploadProgressBar.isVisible = true
                        fileUploadProgressBar.progress = fileUploadState.progress
                    }

                    is FileUploadState.Success -> {
                        fileUploadProgressBar.isVisible = false
                        uploadingFileNameTextView.text =
                            context.getString(
                                R.string.file_uploaded_successfully,
                                fileUploadState.filename
                            )
                    }

                    is FileUploadState.Failure -> {
                        fileUploadProgressBar.isVisible = false
                        uploadingFileNameTextView.text =
                            context.getString(
                                R.string.file_upload_failure,
                                fileUploadState.filename,
                                fileUploadState.message
                            )
                    }
                }

            }
        }
    }

    private class FileUploadStateDiffCallback : DiffUtil.ItemCallback<FileUploadState>() {
        override fun areItemsTheSame(oldItem: FileUploadState, newItem: FileUploadState): Boolean =
            oldItem.filename == newItem.filename

        override fun areContentsTheSame(
            oldItem: FileUploadState,
            newItem: FileUploadState
        ): Boolean = oldItem == newItem
    }
}