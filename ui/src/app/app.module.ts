import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { PostDataBoxComponent } from './post-area/post-data-box/post-data-box.component';
import { PostCreateComponent } from './post-area/post-create/post-create.component';
import { CategoriesModule } from './shared/categories/categories.module';
import { HttpClientModule } from '@angular/common/http';
import { mockApiProvider } from './_helpers/mock-api.service';

@NgModule({
  declarations: [
    AppComponent,
    PostDataBoxComponent,
    PostCreateComponent
  ],
  imports: [
    BrowserModule
    ,CategoriesModule
    ,AppRoutingModule
    ,HttpClientModule
  ],
  exports: [],
  providers: [mockApiProvider],
  bootstrap: [AppComponent]
})
export class AppModule { }
