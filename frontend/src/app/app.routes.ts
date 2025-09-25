import { Routes } from '@angular/router';
import { UserListComponent } from './features/user-list/user-list.component';
import { UserFormComponent } from './features/user-form/user-form.component';
import { HomeComponent } from './features/home/home.component';
import { LoginFormComponent } from './features/login-form/login-form.component';
import { RegisterComponent } from './features/register/register.component';
import { ManageAccountComponent } from './features/manage-account/manage-account.component';
import { RankingListComponent } from './features/ranking-list/ranking-list.component';
import { LandingComponent } from './features/landing/landing.component';
import { AddMediaComponent } from './features/add-media/add-media.component';


export const routes: Routes = [
  { path: 'users', component: UserListComponent },
  { path: 'createuser', component: UserFormComponent },
  { path: 'home', component: HomeComponent },
  { path: 'login', component: LoginFormComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'manageuser', component: ManageAccountComponent },
  { path: 'rankings', component: RankingListComponent },
  { path: '', component: LandingComponent},
  { path: 'add-media/:rankingId/:addMediaRanking/:mediaType', component: AddMediaComponent},
];
