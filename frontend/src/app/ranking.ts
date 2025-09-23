import { MediaList } from './media-list';
import { User } from './user';

export interface Ranking {
  id: number | null;
  userDTO: User | null;
  title: string;
  description: string;
  isPublic: boolean;
  mediaType: string;
  rankingType: string;
}
