import { SUCCESS } from './action-type.util';

export const ACTION_TYPES = {
  ADD_ITEM: 'sidebar/ADD_ITEM',
};

const initialState: Array<any> = [];

export type SidebarItemsState = Readonly<typeof initialState>;

export default (state: SidebarItemsState = initialState, action:any): SidebarItemsState => {
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