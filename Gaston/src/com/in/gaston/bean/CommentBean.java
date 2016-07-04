package com.in.gaston.bean;

public class CommentBean extends CommonBean {
	private String commentId,total_like,total_unlike,like_unlike_status;

	public String getLike_unlike_status() {
		return like_unlike_status;
	}

	public void setLike_unlike_status(String like_unlike_status) {
		this.like_unlike_status = like_unlike_status;
	}

	public String getTotal_like() {
		return total_like;
	}

	public void setTotal_like(String total_like) {
		this.total_like = total_like;
	}

	public String getTotal_unlike() {
		return total_unlike;
	}

	public void setTotal_unlike(String total_unlike) {
		this.total_unlike = total_unlike;
	}

	public String getCommentId() {
		return commentId;
	}

	public void setCommentId(String commentId) {
		this.commentId = commentId;
	}
}
