package org.springframework.batch.sample.validation.valang.custom;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.easymock.MockControl;

import org.springframework.batch.sample.domain.LineItem;

import org.springmodules.validation.valang.functions.Function;
import junit.framework.TestCase;

public class ValidateShippingPricesFunctionTests extends TestCase {

	private ValidateShippingPricesFunction function;
	private MockControl argumentControl;
	private Function argument;

	public void setUp() {
		argumentControl = MockControl.createControl(Function.class);
		argument = (Function) argumentControl.getMock();

		//create function
		function = new ValidateShippingPricesFunction(new Function[] {argument}, 0, 0);
	}
	
	public void testShippingPriceMin() throws Exception {
	
		//create line item with correct shipping price
		LineItem item = new LineItem();
		item.setShippingPrice(new BigDecimal(1.0));
		
		//add it to line items list 
		List items = new ArrayList();
		items.add(item);
		
		//set return value for mock argument
		argument.getResult(null);
		argumentControl.setReturnValue(items,2);
		argumentControl.replay();
		
		//verify result - should be true - all shipping prices are correct
		assertTrue(((Boolean)function.doGetResult(null)).booleanValue());
		
		//now add line item with negative shipping price
		item = new LineItem();
		item.setShippingPrice(new BigDecimal(-1.0));
		items.add(item);
		
		//verify result - should be false - second item has invalid shipping price
		assertFalse(((Boolean)function.doGetResult(null)).booleanValue());
	}
	
	public void testShippingPriceMax() throws Exception {

		//create line item with correct shipping price
		LineItem item = new LineItem();
		item.setShippingPrice(new BigDecimal(99999999.0));
		
		//add it to line items list 
		List items = new ArrayList();
		items.add(item);
		
		//set return value for mock argument
		argument.getResult(null);
		argumentControl.setReturnValue(items,2);
		argumentControl.replay();
		
		//verify result - should be true - all shipping prices are correct
		assertTrue(((Boolean)function.doGetResult(null)).booleanValue());
		
		//now add line item with shipping price above allowed max 
		item = new LineItem();
		item.setShippingPrice(new BigDecimal(100000000.0));
		items.add(item);
		
		//verify result - should be false - second item has invalid shipping price
		assertFalse(((Boolean)function.doGetResult(null)).booleanValue());
	}
}