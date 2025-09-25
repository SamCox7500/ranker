import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { UserService } from '../../services/user.service';
import { User } from '../../core/models/user';
import { ReactiveFormsModule, FormControl, FormGroup, Validators} from '@angular/forms';
import { UserCredentialsDTO } from '../../core/dtos/user-credentials-dto';


@Component({
  selector: 'app-user-form',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './user-form.component.html',
  styleUrl: './user-form.component.css'
})
export class UserFormComponent {
  /*

  userForm = new FormGroup({
    username: new FormControl('', [Validators.required]),
  });

  user: User;
  userCredentials: UserCredentials;

  constructor(private route: ActivatedRoute, private router: Router, private userService: UserService) {
    this.user = new User();
    this.userCredentials = new UserCredentials();
  }

  onSubmit() {
    this.userCredentials.username = this.userForm.value.username || '';
    this.userCredentials.password = this.userForm.value.password || '';
    this.userService.createUser(this.userCredentials).subscribe(result => this.goToUserList());
  }

  goToUserList() {
    this.router.navigate(['/users']);
  }
    */
}
