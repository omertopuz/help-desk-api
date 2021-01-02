import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { PostDetailComponent } from './post-detail/post-detail.component';
import {PostHomeComponent} from './post-home/post-home.component'
import { PostListComponent } from './post-list/post-list.component';


const routes: Routes = [
  {
    path: '',
    component: PostHomeComponent,
    children: [
      {
        path: '',
        component: PostListComponent,
        outlet: 'list-router'
      },
      {
        path: '',
    component: PostDetailComponent,
    outlet: 'detail-router'
      }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class PostsRoutingModule { }
