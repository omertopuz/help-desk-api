import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { CategoryListComponent } from './category/category-list/category-list.component';
import { PostCreateComponent } from './post-area/post-create/post-create.component';
import { PostDataBoxComponent } from './post-area/post-data-box/post-data-box.component';


const routes: Routes = [
  {
    path: 'post-data-box',
    component: PostDataBoxComponent
  },
  {
    path: 'post-create',
    component: PostCreateComponent
  },
  {
  path: 'post-home',
  loadChildren: () => import('./post-area/posts/posts.module').then(m => m.PostsModule),
  data: { preload: true }
},
{ path: '',   redirectTo: '/post-data-box', pathMatch: 'full' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
