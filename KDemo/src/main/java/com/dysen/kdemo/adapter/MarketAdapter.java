package com.dysen.kdemo.adapter;


public class MarketAdapter  {
//    private static Context mContext;
//    private String symbol_market = "";
//    private static final int TYPE_KLINE = 1;
//    private static final int TYPE_GONE = 0;
//    private int TYPE_COLLECTION = 0;
//    private List<PlatformSet> arrayList = new ArrayList<PlatformSet>();
//    private Map<String, String[][]> chartMap = new HashMap<>();
//    private OnItemClickListener mClickListener;
//
//    public MarketAdapter(Context context) {
//        this.mContext = context;
//    }
//
//    public void setData(List<PlatformSet> platformSets) {
//        this.arrayList = platformSets;
//        notifyDataSetChanged();
//    }
//
//    public void putChartMap(String symbol_market, String[][] chartData) {
//        chartMap.put(symbol_market, chartData);
//        notifyDataSetChanged();
//    }
//
//    public void setTypeCollection(int typeCollection) {
//        TYPE_COLLECTION = typeCollection;
//    }
//
//    public void setSymbol_market(String symbol_market_ls) {
//        if (!symbol_market_ls.equals(symbol_market)) {
//            symbol_market = symbol_market_ls;
//        } else {
//            symbol_market = "";
//        }
//        notifyDataSetChanged();
//    }
//
//    public String getSymbol_market() {
//        return symbol_market;
//    }
//
//    public PlatformSet getItem(int position) {
//        if (position >= arrayList.size()) return null;
//        return arrayList.get(position);
//    }
//
//    @Override
//    public int getItemViewType(int position) {
//        if (symbol_market.equals(arrayList.get(position).getSymbol())) {
//            return TYPE_KLINE;
//        } else {
//            return TYPE_GONE;
//        }
//    }
//
//    @Override
//    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        switch (viewType) {
//            case TYPE_KLINE:
//                return new ViewHolder2(LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_market_kline_item, parent, false), mClickListener, TYPE_COLLECTION);
//            case TYPE_GONE:
//                return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_market_item, parent, false), mClickListener, TYPE_COLLECTION);
//        }
//        return null;
//    }
//
//    @Override
//    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//        int type = getItemViewType(position);
//        switch (type) {
//            case TYPE_KLINE:
//                //显示K线逻辑处理
//                final PlatformSet platformSet = arrayList.get(position);
//                ViewHolder2 viewHolder2 = (ViewHolder2) holder;
//                viewHolder2.setData(platformSet);
//                viewHolder2.setChartData(chartMap);
//                break;
//            case TYPE_GONE:
//                //不显示K线逻辑处理
//                ViewHolder viewHolder1 = (ViewHolder) holder;
//                viewHolder1.setData(arrayList.get(position));
//                break;
//        }
//    }
//
//    @Override
//    public int getItemCount() {
//        return arrayList.size();
//    }
//
//    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
//        int mTypeCollection = 0;
//        OnItemClickListener mListener;
//        ImageView img_market_type, img_collect;
//        TextView tv_currency_name, tv_exchange_type, tv_jylx, tv_currentPrice, tv_currentPrice_d, tv_market_rate, tv_turnover;
//        View ll_market_rate, ll_market_info;
//
//        public ViewHolder(View view, OnItemClickListener listener, int typeCollection) {
//            super(view);
//            mListener = listener;
//            mTypeCollection = typeCollection;
//            this.img_collect = view.findViewById(R.id.img_collect);
//            this.img_market_type = view.findViewById(R.id.img_market_type);
//            this.tv_jylx = view.findViewById(R.id.tv_jylx);
//            this.tv_turnover = view.findViewById(R.id.tv_turnover);
//            this.tv_currency_name = view.findViewById(R.id.tv_currency_name);
//            this.tv_exchange_type = view.findViewById(R.id.tv_exchange_type);
//            this.tv_currentPrice = view.findViewById(R.id.tv_currentPrice);
//            this.tv_currentPrice_d = view.findViewById(R.id.tv_currentPrice_d);
//            this.tv_market_rate = view.findViewById(R.id.tv_market_rate);
//            this.ll_market_rate = view.findViewById(R.id.ll_market_rate);
//            this.ll_market_info = view.findViewById(R.id.ll_market_info);
//            this.ll_market_info.setOnClickListener(this);
//        }
//
//        public void setData(final PlatformSet platformSet) {
//            try {
//                CurrencyData currencyData = platformSet.getCurrencyData();
//                CurrencyData exchangeData = platformSet.getExchangeData();
//                TickerData tickerData = platformSet.getTickerData();
//                if (mTypeCollection == 1) {
//                    tv_exchange_type.setVisibility(View.VISIBLE);
//                } else {
//                    tv_exchange_type.setVisibility(View.GONE);
//                }
//                if (mTypeCollection == 0 && platformSet.isCollected()) {
//                    img_collect.setVisibility(View.VISIBLE);
//                } else {
//                    img_collect.setVisibility(View.GONE);
//                }
//                String riseRate = getRate(tickerData.getRiseRate());
//                String vol = getVol(tickerData.getVol());
//                int color_id = ZBColor.INSTANCE.getRed(mContext);
//                int drawable_id = R.drawable.bnt_login;
//                if (riseRate.indexOf("-") != -1) {
//                    color_id = ZBColor.INSTANCE.getGreen(mContext);
//                    drawable_id = R.drawable.bnt_login_green;
//                }
//                tv_currency_name.setText(platformSet.getCurrencyType());
//                tv_exchange_type.setText("/" + platformSet.getExchangeType());
//                tv_market_rate.setText(riseRate);
//                ll_market_rate.setBackgroundResource(drawable_id);
//                tv_jylx.setText(String.format(Tools.getString(R.string.market_24xsl), vol));
//                //tv_turnover.setText(String.format("≈ %s %s", getVol(tickerData.getTurnoverString()), exchangeData.getSymbol()));
//                tv_currentPrice.setTextColor(color_id);
//                tv_currentPrice.setText(exchangeData.getSymbol() + deFormat(tickerData.getLast(), -8));
//                tv_currentPrice_d.setText(Constants.CurrencyType.CNY.symbol() + deFormat(tickerData.getLastRmb(), -8));
//                Glide.with(mContext).load(currencyData.iconUrl64()).error(R.mipmap.btn_market_unknown).into(img_market_type);
//            } catch (Exception e) {
//
//            }
//        }
//
//        @Override
//        public void onClick(View v) {
//            if (mListener != null) {
//                mListener.onItemClick(v, getPosition(), 0);
//            }
//        }
//    }
//
//    static class ViewHolder2 extends ViewHolder implements MagicIndicatorView.IOnListener {
//        TextView tv_zx;
//        ImageView iv_zx;
//        ItemKview linechart;
//        LoadingFrameLayout fl_loading;
//        LinearLayout ll_zx, ll_transaction, ll_price_reminding;
//        MagicIndicatorView magicIndicatorView;
//        List<KLineItemName> itemNameList = new ArrayList<KLineItemName>();
//        int magicIndicator_index = 0;
//        PlatformSet platformSet_ls = null;
//        KLineItemName kLineItemName_ls = null;
//        Map<String, String[][]> chartMap_ls = null;
//
//        public ViewHolder2(View view, OnItemClickListener listener, int typeCollection) {
//            super(view, listener, typeCollection);
//            initItemName();
//            this.ll_zx = view.findViewById(R.id.ll_zx);
//            this.ll_transaction = view.findViewById(R.id.ll_transaction);
//            this.ll_price_reminding = view.findViewById(R.id.ll_price_reminding);
//            this.iv_zx = view.findViewById(R.id.iv_zx);
//            this.tv_zx = view.findViewById(R.id.tv_zx);
//            this.linechart = view.findViewById(R.id.linechart);
//            this.linechart.setOnClickListener(this);
//            this.fl_loading = view.findViewById(R.id.fl_loading);
//            this.magicIndicatorView = view.findViewById(R.id.magicIndicatorView);
//            this.ll_zx.setOnClickListener(this);
//            this.ll_transaction.setOnClickListener(this);
//            this.ll_price_reminding.setOnClickListener(this);
//            this.magicIndicatorView.setTitle_list2(itemNameList);
//            this.magicIndicatorView.initView();
//            this.magicIndicatorView.setiOnListener(this);
//        }
//
//        @Override
//        public void setData(final PlatformSet platformSet) {
//            super.setData(platformSet);
//            platformSet_ls = platformSet;
//            if (platformSet.isCollected()) {
//                iv_zx.setImageResource(R.mipmap.icon_collect_on);
//                tv_zx.setText(Tools.getString(R.string.cancel_market_optional));
//            } else {
//                iv_zx.setImageResource(R.mipmap.icon_collect_off);
//                tv_zx.setText(Tools.getString(R.string.add_market_optional));
//            }
//
//        }
//
//        public void setChartData(final Map<String, String[][]> chartMap) {
//            chartMap_ls = chartMap;
//            final String[][] chartData = chartMap.get(platformSet_ls.getSymbol() + "_" + kLineItemName_ls.getTimeInterval());
//            createChartDataSet(fl_loading, linechart, chartData);
//        }
//
//        @Override
//        public void onItem(int index) {
//            magicIndicator_index = index;
//            kLineItemName_ls = itemNameList.get(index);
//            if (mListener != null) {
//                final String[][] chartData = chartMap_ls.get(platformSet_ls.getSymbol() + "_" + kLineItemName_ls.getTimeInterval());
//                if (chartData == null) {
//                    if (!fl_loading.isShown()) {
//                        fl_loading.setVisibility(View.VISIBLE);
//                    }
//                }
//                linechart.setType(kLineItemName_ls.getKlineType());
//                mListener.onItemClick(null, getPosition(), kLineItemName_ls.getTimeInterval());
//            }
//        }
//
//        private void initItemName() {
//            itemNameList = new ArrayList<KLineItemName>();
//            itemNameList.add(new KLineItemName(1, Tools.getString(R.string.time_min), 1 * 60));
//            itemNameList.add(new KLineItemName(0, Tools.getString(R.string.min_k), 15 * 60));
//            itemNameList.add(new KLineItemName(0, Tools.getString(R.string.hours_k), 60 * 60));
//            itemNameList.add(new KLineItemName(0, Tools.getString(R.string.days_k), 24 * 60 * 60));
//            itemNameList.add(new KLineItemName(0, Tools.getString(R.string.weeks_k), 7 * 24 * 60 * 60));
//            kLineItemName_ls = itemNameList.get(0);
//        }
//    }
//
//    public void setOnItemClickListener(OnItemClickListener listener) {
//        this.mClickListener = listener;
//    }
//
//    public interface OnItemClickListener {
//        void onItemClick(View view, int position, int timeInterval);
//    }
//
//    //获取带单位的成交量
//    public static String getVol(String vol) {
//        String dw_str = "";
//        double vol_a = toDouble(vol);
//        if (ChangeLanguageHelper.getAppLanguage() == 1) {
//            if (vol_a > 10000) {
//                vol_a = vol_a / 10000.0;
//                dw_str = Tools.getString(R.string.market_dw_w);
//            }
//        } else {
//            if (vol_a > 1000000) {
//                vol_a = vol_a / 1000000;
//                dw_str = Tools.getString(R.string.market_dw_bw);
//            } else if (vol_a > 1000) {
//                vol_a = vol_a / 1000.0;
//                dw_str = Tools.getString(R.string.market_dw_q);
//            }
//        }
//        return deFormat(vol_a + "", -2) + dw_str;
//    }
//
//    //获取涨跌幅
//    public static String getRate(String rate) {
//        if (rate != null) {
//            //是否超出尾数限制
//            String str_rate = rate;
//            if (str_rate.indexOf(".") > 0 && str_rate.length() > 3) {
//                String str_1 = str_rate.substring(0, str_rate.indexOf(".") + 1);
//                String str_2 = str_rate.substring(str_rate.indexOf(".") + 1, str_rate.length());
//                if (str_2.length() > 2) {
//                    str_2 = str_2.substring(0, 2);
//                }
//                str_rate = str_1 + str_2;
//            }
//            if (str_rate.indexOf("-") == -1) {
//                rate = "+" + str_rate + "%";
//            } else {
//                rate = str_rate + "%";
//            }
//        } else {
//            rate = "+0.00%";
//        }
//        return rate;
//    }
//
//    public static void createChartDataSet(final LoadingFrameLayout fl_loading, final ItemKview linechart, String[][] marketChartDatas) {
//        List<MarketChartData> marketChartDataLists = new ArrayList<MarketChartData>();
//        if (marketChartDatas != null && marketChartDatas.length > 0) {
//            for (int i = 0; i < marketChartDatas.length; i++) {
//                marketChartDataLists.add(new MarketChartData(marketChartDatas[i]));
//            }
//            //更新图表
//            try {
//                linechart.setOHLCData(marketChartDataLists);
//                linechart.setYIsLeft(false);
//                linechart.setHaveBorder(false);
//                linechart.setShowXText(true);
//                linechart.setShowYText(true);
//                if (linechart.getType() == 1) {
//                    //分时
//                    linechart.setLatLine(3);
//                    linechart.setLogLine(6);
//                    linechart.setHaveBorder(false);
//                    linechart.setChartTop(10);
//                    linechart.setHaveBorder(true);
//                    linechart.setChartBottom(10);
//                    linechart.setLongitudeIsShow(true);
//                } else {
//                    //k线
//                    linechart.setLatLine(3);
//                    linechart.setLogLine(6);
//                    linechart.setHaveBorder(false);
//                    linechart.setChartTop(2 * linechart.DEFAULT_AXIS_TITLE_SIZE);
//                    linechart.setChartBottom(3 * linechart.DEFAULT_AXIS_TITLE_SIZE);
//                    linechart.setHaveBorder(true);
//                    linechart.setLongitudeIsShow(true);
//                }
//                linechart.postInvalidate();
//                fl_loading.setVisibility(View.GONE);
//            } catch (Exception e) {
//            }
//        } else {
//            if (!fl_loading.isShown()) {
//                fl_loading.setVisibility(View.VISIBLE);
//            }
//            linechart.clearOHLCData();
//        }
//    }

}
