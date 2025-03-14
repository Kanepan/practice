package com.kane.practice.tools;

public class SqlCreator {


    public static void main(String[] args) {

        String sql = "INSERT INTO `hbl_base`.`b_capital_risk_config` ( `company_id`, `merchant_id`, `machine_id`, `risk_code`, `type`, `priority`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ( 0, 2465, NULL, 'DEVICE_DEBT', 1, 0, 1, 'admin', '2025-02-10 14:01:40', 'admin', '2025-02-10 14:01:40');";

        String merchantIds = "3930\n" +
                "3940\n" +
                "3831\n" +
                "3883\n" +
                "3945\n" +
                "3950\n" +
                "3954\n" +
                "3967\n" +
                "3973\n" +
                "3719\n" +
                "2665\n" +
                "3984\n" +
                "4000\n" +
                "4002\n" +
                "3996\n" +
                "4008\n" +
                "3987\n" +
                "3981\n" +
                "4020\n" +
                "4034\n" +
                "4040\n" +
                "4042\n" +
                "4053\n" +
                "4054\n" +
                "4062\n" +
                "4065\n" +
                "4077\n" +
                "4059\n" +
                "4043\n" +
                "3205\n" +
                "4088\n" +
                "4093\n" +
                "4129\n" +
                "4138\n" +
                "4139\n" +
                "2586\n" +
                "3110\n" +
                "4149\n" +
                "4154\n" +
                "4155\n" +
                "4164\n" +
                "4176\n" +
                "4157\n" +
                "3127\n" +
                "4130\n" +
                "3928\n" +
                "4219\n" +
                "4227\n" +
                "4225\n" +
                "4180\n" +
                "4192\n" +
                "4230\n" +
                "3484\n" +
                "4238\n" +
                "4240\n" +
                "4247\n" +
                "4248\n" +
                "4244\n" +
                "4261\n" +
                "4265\n" +
                "4357\n" +
                "4373\n" +
                "4376\n" +
                "4387\n" +
                "4390\n" +
                "4392\n" +
                "4391\n" +
                "4386\n" +
                "4397\n" +
                "4264\n" +
                "4417\n" +
                "4216\n" +
                "4061\n" +
                "4424\n" +
                "4200\n" +
                "4407\n" +
                "4453\n" +
                "4452\n" +
                "3549\n" +
                "3522\n" +
                "4209\n" +
                "3735\n" +
                "3899\n" +
                "3752\n" +
                "3751\n" +
                "4251\n" +
                "3897\n" +
                "4221\n" +
                "4401\n" +
                "4574\n" +
                "4805\n" +
                "4806\n" +
                "4998\n" +
                "4461\n" +
                "2658\n" +
                "4483\n" +
                "4484\n" +
                "4477\n" +
                "4489\n" +
                "4368\n" +
                "4492\n" +
                "4500\n" +
                "4451\n" +
                "4499\n" +
                "4508\n" +
                "4509\n" +
                "4511\n" +
                "4515\n" +
                "4519\n" +
                "3634\n" +
                "4528\n" +
                "4533\n" +
                "4538\n" +
                "2973\n" +
                "4544\n" +
                "4525\n" +
                "4554\n" +
                "4445\n" +
                "4557\n" +
                "4552\n" +
                "4534\n" +
                "4377\n" +
                "4532\n" +
                "4589\n" +
                "4593\n" +
                "4592\n" +
                "4603\n" +
                "4608\n" +
                "4613\n" +
                "4618\n" +
                "4482\n" +
                "4623\n" +
                "4628\n" +
                "4637\n" +
                "4638\n" +
                "4639\n" +
                "4640\n" +
                "4643\n" +
                "4642\n" +
                "4653\n" +
                "4501\n" +
                "3244\n" +
                "4668\n" +
                "4680\n" +
                "4682\n" +
                "4675\n" +
                "4695\n" +
                "4694\n" +
                "4662\n" +
                "4684\n" +
                "4612\n" +
                "4677\n" +
                "4697\n" +
                "4708\n" +
                "4714\n" +
                "4718\n" +
                "4720\n" +
                "4722\n" +
                "4731\n" +
                "4725\n" +
                "4732\n" +
                "4739\n" +
                "4740\n" +
                "4742\n" +
                "4549\n" +
                "4748\n" +
                "4751\n" +
                "4716\n" +
                "4754\n" +
                "4758\n" +
                "4761\n" +
                "4239\n" +
                "4763\n" +
                "4701\n" +
                "4759\n" +
                "4769\n" +
                "4790\n" +
                "4794\n" +
                "4719\n" +
                "4804\n" +
                "4808\n" +
                "4814\n" +
                "4819\n" +
                "4820\n" +
                "4803\n" +
                "4825\n" +
                "2505\n" +
                "4818\n" +
                "4775\n" +
                "4830\n" +
                "4231\n" +
                "4833\n" +
                "4829\n" +
                "4777\n" +
                "4838\n" +
                "4852\n" +
                "4767\n" +
                "4853\n" +
                "4781\n" +
                "4780\n" +
                "4859\n" +
                "4867\n" +
                "4865\n" +
                "4864\n" +
                "4880\n" +
                "4885\n" +
                "4891\n" +
                "4866\n" +
                "4834\n" +
                "4897\n" +
                "4837\n" +
                "4912\n" +
                "4665\n" +
                "4919\n" +
                "4674\n" +
                "4894\n" +
                "4924\n" +
                "4929\n" +
                "4937\n" +
                "4940\n" +
                "4947\n" +
                "4953\n" +
                "4868\n" +
                "4949\n" +
                "4746\n" +
                "4956\n" +
                "4873\n" +
                "4968\n" +
                "4969\n" +
                "4955\n" +
                "4977\n" +
                "4881\n" +
                "4877\n" +
                "4983\n" +
                "4879\n" +
                "4991\n" +
                "4994\n" +
                "4996\n" +
                "4980\n" +
                "4925\n" +
                "5007\n" +
                "4466\n" +
                "5004\n" +
                "5015\n" +
                "5027\n" +
                "4874\n" +
                "4961\n" +
                "5030\n" +
                "5045\n" +
                "5049\n" +
                "5054\n" +
                "5057\n" +
                "5059\n" +
                "5061\n" +
                "5036\n" +
                "4938\n" +
                "5070\n" +
                "5074\n" +
                "5051\n" +
                "5076\n" +
                "5084\n" +
                "5083\n" +
                "5088\n" +
                "5087\n" +
                "5093\n" +
                "5075\n" +
                "5092\n" +
                "5065\n" +
                "4772\n" +
                "5095\n" +
                "4928\n" +
                "4927\n" +
                "5097\n" +
                "5098\n" +
                "5091\n" +
                "5100\n" +
                "5080\n" +
                "4524\n" +
                "4931\n" +
                "4688\n" +
                "5069\n" +
                "4966\n" +
                "5006\n" +
                "5032\n" +
                "4901\n" +
                "5047\n" +
                "5042\n" +
                "4459\n" +
                "5109\n" +
                "5158\n" +
                "5246\n" +
                "5368\n" +
                "5223\n" +
                "5505\n" +
                "5524\n" +
                "5562\n" +
                "5634\n" +
                "5647\n" +
                "5655\n" +
                "5667\n" +
                "5711\n" +
                "5106\n" +
                "5125\n" +
                "5114\n" +
                "5112\n" +
                "5127\n" +
                "5132\n" +
                "5138\n" +
                "5140\n" +
                "4414\n" +
                "4355\n" +
                "5144\n" +
                "5153\n" +
                "5165\n" +
                "5166\n" +
                "5134\n" +
                "5169\n" +
                "5171\n" +
                "3790\n" +
                "5176\n" +
                "5181\n" +
                "5174\n" +
                "5063\n" +
                "5204\n" +
                "5205\n" +
                "4959\n" +
                "5189\n" +
                "5233\n" +
                "5236\n" +
                "5206\n" +
                "5256\n" +
                "5259\n" +
                "5264\n" +
                "5266\n" +
                "5220\n" +
                "5232\n" +
                "5271\n" +
                "5219\n" +
                "5272\n" +
                "5278\n" +
                "5228\n" +
                "4648\n" +
                "5285\n" +
                "5292\n" +
                "5293\n" +
                "5299\n" +
                "5309\n" +
                "5288\n" +
                "4858\n" +
                "5210\n" +
                "5313\n" +
                "5323\n" +
                "5202\n" +
                "5119\n" +
                "5327\n" +
                "5068\n" +
                "5331\n" +
                "5332\n" +
                "5334\n" +
                "5338\n" +
                "5337\n" +
                "5339\n" +
                "5137\n" +
                "5284\n" +
                "5349\n" +
                "5350\n" +
                "5352\n" +
                "5356\n" +
                "5341\n" +
                "5359\n" +
                "5360\n" +
                "5358\n" +
                "5364\n" +
                "5369\n" +
                "5373\n" +
                "5002\n" +
                "5376\n" +
                "5378\n" +
                "5183\n" +
                "5394\n" +
                "5383\n" +
                "4941\n" +
                "5396\n" +
                "5412\n" +
                "5418\n" +
                "5419\n" +
                "5423\n" +
                "5428\n" +
                "5437\n" +
                "5440\n" +
                "5442\n" +
                "5443\n" +
                "5445\n" +
                "5413\n" +
                "5457\n" +
                "5447\n" +
                "5469\n" +
                "5453\n" +
                "5473\n" +
                "5474\n" +
                "5451\n" +
                "5477\n" +
                "5462\n" +
                "5485\n" +
                "5486\n" +
                "5488\n" +
                "5464\n" +
                "5489\n" +
                "5500\n" +
                "5492\n" +
                "5521\n" +
                "5522\n" +
                "4721\n" +
                "5495\n" +
                "5535\n" +
                "5537\n" +
                "5539\n" +
                "5540\n" +
                "5536\n" +
                "5553\n" +
                "5559\n" +
                "5563\n" +
                "5564\n" +
                "5573\n" +
                "5576\n" +
                "5577\n" +
                "5578\n" +
                "5452\n" +
                "5544\n" +
                "5589\n" +
                "5597\n" +
                "5600\n" +
                "5602\n" +
                "5592\n" +
                "5611\n" +
                "5606\n" +
                "5557\n" +
                "5615\n" +
                "5609\n" +
                "5617\n" +
                "5621\n" +
                "5625\n" +
                "5641\n" +
                "5652\n" +
                "5630\n" +
                "5645\n" +
                "5663\n" +
                "5665\n" +
                "5670\n" +
                "5669\n" +
                "5668\n" +
                "5648\n" +
                "5675\n" +
                "5676\n" +
                "5580\n" +
                "5677\n" +
                "5664\n" +
                "5682\n" +
                "5684\n" +
                "2828\n" +
                "5685\n" +
                "5694\n" +
                "5695\n" +
                "5700\n" +
                "5709\n" +
                "5638\n" +
                "5717\n" +
                "5627\n" +
                "5687\n" +
                "5143\n" +
                "5516\n" +
                "4771\n" +
                "5550\n" +
                "4462\n" +
                "5454\n" +
                "4463\n" +
                "5722\n" +
                "5794\n" +
                "5946\n" +
                "6291\n" +
                "6355\n" +
                "5719\n" +
                "5729\n" +
                "5732\n" +
                "5736\n" +
                "5739\n" +
                "5307\n" +
                "5745\n" +
                "5725\n" +
                "5692\n" +
                "5713\n" +
                "5754\n" +
                "5773\n" +
                "4875\n" +
                "5802\n" +
                "5800\n" +
                "4909\n" +
                "5832\n" +
                "5804\n" +
                "5837";

        String[] merchantIdArray = merchantIds.split("\n");
        for (String merchantId : merchantIdArray) {
            System.out.println(sql.replace("2465", merchantId));
        }
    }
}
