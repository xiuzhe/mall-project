package com.ishare.mall.common.base.constant.view;

/**
 * Created by YinLin on 2015/9/22. 
 * Description : Version 1.0
 */
public interface ManageViewConstant {
	
	interface Order {
		String LIST_ORDER = "order/list";
		String EDIT_ORDER = "order/edit";
		String DELIVER_ORDER = "order/deliver";
		String LOGISTICS_ORDER = "order/logistics";
		String CANCEL_ORDER = "order/cancel";
		String ORDER_DETAIL = "order/detail";
		String REFUND_MONEY = "order/refund/list_money";
		String REFUND_PRODUCT = "order/refund/list_product";
		String REFUND_MONEY_DETAIL = "order/refund/detail";
		String FORWARD_TO_REFUND_PAGE = "order/refund/confirm";
	}
	
	interface Category {
		String LIST_CATEGORY = "category/list";
		String ADD_CATEGORY ="category/add";
	}
	
	interface Index {
		String LOGIN = "index/login";
    }
	
	interface Main {
		String MAIN = "main/index";

	}
	interface Welcome {
		String WELCOME = "welcome/index";
		String FINDTHISWEEK ="member/list";

	}
	interface  Member {
		String LIST_THISWEEKMEMBER = "member/list";
	}

	interface Channel{
		String CHANNEL_PAGE = "channel/list";
		String CHANNEL_VIEW = "channel/view";
		String SUCCESS = "S";
		String FORWARD_TO_ADD_PAGE = "channel/add";
		String FORWARD_TO_UPDATE_PAGE = "channel/update";
		String LIST_THISWEEKMEMBER="channel/thisweekadd";
	}
	
	interface Product {
		 String ADD_PRODUCT ="product/add";
      	 String LIST_PRODUCT ="product/list";
		 String LIST_PRODUCT_THISWEEK="product/thisweeklist";
      	 String UPDATE_PRODUCT= "product/update";
	}
	
    interface Brand {
   	 String LIST_BRAND ="brand/list";
   	 String ADD_BRAND ="brand/add";
   	 String BRAND_UPDATE_SUCCESS = "S";
   	 String BRAND_ADD_SUCCESS = "S";
   	 String BRAND_UPDATE = "brand/update";
   }
}
