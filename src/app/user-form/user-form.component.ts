import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { UserService } from '../user.service';
import { User } from '../user';
import { ReactiveFormsModule, FormControl, FormGroup, Validators} from '@angular/forms';


@Component({
  selector: 'app-user-form',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './user-form.component.html',
  styleUrl: './user-form.component.css'
})
export class UserFormComponent {

  userForm = new FormGroup({
    username: new FormControl('', [Validators.required]),
  });

  user: User

  constructor(private route: ActivatedRoute, private router: Router, private userService: UserService) {
    this.user = new User();
  }

  onSubmit() {
    this.user.username = this.userForm.value.username || '';
    this.userService.save(this.user).subscribe(result => this.goToUserList());
  }

  goToUserList() {
    this.router.navigate(['/users']);
  }

}
