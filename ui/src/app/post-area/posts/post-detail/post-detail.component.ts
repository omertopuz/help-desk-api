import { Component, OnInit } from '@angular/core';
import { WPostDetail } from 'src/app/_models/view/w-post-detail';

@Component({
  selector: 'app-post-detail',
  templateUrl: './post-detail.component.html',
  styleUrls: ['./post-detail.component.css']
})
export class PostDetailComponent implements OnInit {

  postDetail:WPostDetail

  constructor() { }

  ngOnInit() {
    this.getDetail();
  }

  getDetail(){

    this.postDetail={
      postId: "cdcewce",
      title: "Post Item -1",
      content: "Content - 1",
      categoryId: "Category"
    };


    // this.postDetail= new WPostDetail();
    // this.postDetail.postId = "cdcewce";
    // this.postDetail.title = "Post Item -1";
    // this.postDetail.content = "Content - 1";
    // this.postDetail.categoryId = "Category";
  }

}
