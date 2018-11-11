/**
 * 
 */
$(function() {
	$(".add-selected").click(function() {
		// 找到勾选了的【未选中角色】
		$(".unselect-roles ul li input:checked").each(function(index, input) {
			// 取消选中
			$(input).prop("checked", false);

			// 找到input的li
			// 如果两边都要有，那么就在最后加上.clone()，拷贝一份
			var li = $(input).parent().parent();
			$(".selected-roles ul").append(li);
		});
	});

	$(".add-all").click(function() {
		$(".unselect-roles ul li input").each(function(index, input) {
			$(input).prop("checked", false);
			var li = $(input).parent().parent();
			$(".selected-roles ul").append(li);
		});
	});

	$(".remove-selected").click(function() {
		// 找到勾选了的【选中角色】
		$(".selected-roles ul li input:checked").each(function(index, input) {
			// 取消选中
			$(input).prop("checked", false);
			var li = $(input).parent().parent();
			$(".unselect-roles ul").append(li);
		});
	});
	$(".remove-all").click(function() {
		$(".selected-roles ul li input").each(function(index, input) {
			$(input).prop("checked", false);
			var li = $(input).parent().parent();
			$(".unselect-roles ul").append(li);
		});
	});

	// 在提交表单的时候，把选中的角色全部勾选
	// 另外，未选中的角色，要全部取消勾选
	// 最后，还需要把勾选的input的name属性中的数字，替换成0\1\2这种形式
	$(".select-role-form").bind("submit", function() {
		$(".select-role-form .selected-roles ul li input").each(function(index, input) {
			$(input).prop("checked", true);// 勾选

			// 替换name中的数字
			var name = $(input).attr("name");
			name = name.replace(/\d+/, index);
			$(input).attr("name", name);
		});

		$(".select-role-form .unselect-roles ul li input").each(function(index, input) {
			$(input).prop("checked", false);// 取消勾选
		});
	});
});