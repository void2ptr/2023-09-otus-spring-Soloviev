import {
  Component

} from '@angular/core';
import { CommonModule } from '@angular/common';
import { AuthorsComponent } from './components/authors/authors.component'


@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss',
  providers: [AuthorsComponent]
})
export class AppComponent {
  title = 'Books-SPA';

  constructor( private AuthorsComponent : AuthorsComponent) {}
}
