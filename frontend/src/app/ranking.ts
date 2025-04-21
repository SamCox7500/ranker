import { User } from './user';

export interface Ranking {
  id: number | null;
  userDTO: User | null;
  title: string;
  description: string;
  isPublic: boolean;
  isReverseOrder: boolean;
  mediaType: string;
}
