import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { PostsRoutingModule } from './posts-routing.module';
import { PostListComponent } from './post-list/post-list.component';
import { PostDetailComponent } from './post-detail/post-detail.component';
import { PostHomeComponent } from './post-home/post-home.component';
import { CategoriesModule } from 'src/app/shared/categories/categories.module';

@NgModule({
  declarations: [PostListComponent, PostDetailComponent, PostHomeComponent],
  exports:[],
  imports: [
    CommonModule,CategoriesModule,
    PostsRoutingModule
  ]
})
export class PostsModule { }
