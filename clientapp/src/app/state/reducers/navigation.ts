import { SUCCESS } from './action-type.util';

export const ACTION_TYPES = {
  ADD_ITEM: 'navigation/ADD_ITEM',
};

const initialState: Array<any> = [];

export type NavigationItemsState = Readonly<typeof initialState>;

export default (state: NavigationItemsState = initialState, action:any): NavigationItemsState => {
  switch (action.type) {
    case SUCCESS(ACTION_TYPES.ADD_ITEM): {
      const { data } = action.payload;
      return [
        ...state,
        data
      ];
    }
    default:
      return state;
  }
};